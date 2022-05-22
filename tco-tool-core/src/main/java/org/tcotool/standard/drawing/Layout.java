package org.tcotool.standard.drawing;

import ch.softenvironment.util.Tracer;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureEnumeration;

/**
 * Layout algorithm to auto-layout several nodes in 2D space.
 *
 * @author ce
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class Layout {

    // TODO make this class reusable for other similar graphic-tasks
    private static final double delta1 = 1.0;// node_distribution
    private static final double delta2 = 0.01;// borderline
    private static final double delta3 = 0.0001;// edge length
    private static final double delta4 = 100.0;// edge crossing
    private static final double delta10 = 0.75;// cooling 0.6..0.95
    private static final double delta11 = 0.75;// cooling 0.6..0.95
    private double min_x;
    private double max_x;
    private double min_y;
    private double max_y;
    private double maxdist;
    private java.util.List<NodeFigure> nodev;
    private java.util.List<EdgeFigure> edgev;
    private int oldpos_i;
    private double oldpos_x;
    private double oldpos_y;

    private double borderline() {
        double sum = 0.0;
        for (int i = 0; i < nodev.size(); i++) {
            double dx1 = getNode(i).getEast() - min_x;
            double dy1 = getNode(i).getSouth() - min_y;
            double dx2 = max_x - getNode(i).getEast();
            double dy2 = max_y - getNode(i).getSouth();
            double f = 0.0;
            if (dx1 != 0.0) {
                f = f + 1.0 / dx1 / dx1;
            } else {
                f = f + 1.0;
            }
            if (dy1 != 0.0) {
                f = f + 1.0 / dy1 / dy1;
            } else {
                f = f + 1.0;
            }
            if (dx2 != 0.0) {
                f = f + 1.0 / dx2 / dx2;
            } else {
                f = f + 1.0;
            }
            if (dy2 != 0.0) {
                f = f + 1.0 / dy2 / dy2;
            } else {
                f = f + 1.0;
            }
            sum = sum + delta2 * f;
        }
        return sum;
    }

    private double cost() {
        // node distribution
        double a = 0.0;
        if (delta1 > 0.0) {
            a = nodeDistribution();
        }
        // borderline
        double b = 0.0;
        if (delta2 > 0.0) {
            b = borderline();
        }
        // edge length
        double c = 0.0;
        if (delta3 > 0.0) {
            c = edgelength();
        }
        // edge crossing
        double d = 0.0;
        if (delta4 > 0.0) {
            d = edgecrossing();
        }
        // node-edge distance
        // e=nodeedgedistances();

        // ch.softenvironment.util.Tracer.getInstance().debug("a "+Double.toString(a)+"; b "+Double.toString(b)+"; c "+Double.toString(c)+"; d "+Double.toString(d));
        return a + b + c + d;
    }

    private void doit() {
        double temp = 1.0;

        // ch.softenvironment.util.Tracer.getInstance().debug(dumpNodes());

        maxdist = getdist(min_x, min_y, max_x, max_y);
        double radius = maxdist / 2.0;
        double e = cost();
        double ep;
        int max_stages = 10;
        int nodev_size = nodev.size();
        int max_trials = 30 * nodev_size;
        if (nodev_size > 30) {
            max_stages = (int) Math.sqrt(10000.0 / (double) nodev_size / 2.0);
            // ch.softenvironment.util.Tracer.getInstance().debug("max_stages "+Integer.toString(max_stages));
            max_trials = nodev_size;
            // TODO should throw an exception
            Tracer.getInstance().runtimeWarning("stop layout; would run too long");
            return;
        }
        for (int stages = 0; stages < max_stages; stages++) {
            for (int trials = 0; trials < max_trials; trials++) {
                neighborhood(radius);
                ep = cost();
                // ch.softenvironment.util.Tracer.getInstance().debug("ep "+Double.toString(ep)+"; e "+Double.toString(e));

                // r = exp((e-ep)/temp)
                // RoseApp.WriteErrorLog "r "+str(r)
                // If (ep<e) Or (myrandom<r) Then
                if (ep < e) {
                    e = ep;
                    // ch.softenvironment.util.Tracer.getInstance().debug(dumpNodes());
                } else {
                    // restore moved item to old position
                    moveback();
                }
            }
            radius = radius * delta11;
            temp = temp * delta10;
        }
    }

    /**
     * Layout the Figures within the given view.
     *
     * @param diagView
     */
    public static void doLayout(DependencyView view) {
        java.util.Vector<EdgeFigure> edgev = new java.util.Vector<EdgeFigure>(30);
        java.util.Vector<NodeFigure> nodev = new java.util.Vector<NodeFigure>(30);
        // collect nodes and edges from current diagram
        /*
         * // Var. I: collect by model org.tcotool.presentation.Diagram
         * diag=view.getDiagram(); java.util.Iterator
         * elei=diag.getPresentationElement().iterator(); while(elei.hasNext()){
         * Object obj=elei.next(); if(obj instanceof
         * org.tcotool.presentation.PresentationEdge){ edgev.add(obj); }else
         * if(obj instanceof org.tcotool.presentation.PresentationNode){
         * nodev.add(obj); } }
         */
        // Var.II: collect by Figure's
        FigureEnumeration enumeration = view.drawing().figures();
        while (enumeration.hasNextFigure()) {
            Figure figure = enumeration.nextFigure();
            if (figure instanceof EdgeFigure) {
                edgev.add((EdgeFigure) figure);
            } else if (figure instanceof NodeFigure) {
                nodev.add((NodeFigure) figure);
            }
        }
        // dumpNodes(nodev.iterator()); // before
        Layout model = new Layout();
        // Tracer.getInstance().debug("layout diagram","H "+view.getMaximumSize().getHeight()+", W"+view().getWidth());
        model.layout(nodev, edgev, 0, 0, view.getWidth(), view.getHeight());

        // dumpNodes(nodev.iterator()); // after
    }

    private boolean edgecross(int edge1, int edge2) {
        double x1 = getBeginNode(edge1).getEast();
        double y1 = getBeginNode(edge1).getSouth();
        double x2 = getEndNode(edge1).getEast();
        double y2 = getEndNode(edge1).getSouth();
        double x3 = getBeginNode(edge2).getEast();
        double y3 = getBeginNode(edge2).getSouth();
        double x4 = getEndNode(edge2).getEast();
        double y4 = getEndNode(edge2).getSouth();
        if (x2 == x1) {
            if (x4 == x3) {
                return false;
            }
            double t34 = (y4 - y3) / (x4 - x3);
            double ys3 = (x1 - x3) * t34;
			if ((y4 - y3 > 0) && (ys3 > 0) && (ys3 < y4 - y3)) {
				return true;
			} else {
				return (y3 - y4 > 0) && (ys3 < 0) && (ys3 > y4 - y3);
			}
        }
        double t12 = (y2 - y1) / (x2 - x1);
        if (x3 == x4) {
            double ys1 = (x4 - x1) * t12;
			if ((y2 - y1 > 0) && (ys1 > 0) && (ys1 < y2 - y1)) {
				return true;
			} else {
				return (y1 - y2 > 0) && (ys1 < 0) && (ys1 > y2 - y1);
			}
        }
        double t34 = (y4 - y3) / (x4 - x3);
        // parallel?
        if (t12 == t34) {
            return false;
        }
        double xs1 = (y3 - y1 - (x3 - x1) * t34) / (t12 - t34);
		if ((x2 - x1 > 0) && (xs1 > 0) && (xs1 < x2 - x1)) {
			return true;
		} else {
			return (x1 - x2 > 0) && (xs1 < 0) && (xs1 > x2 - x1);
		}
    }

    private double edgecrossing() {
        double sum = 0;
        for (int i = 0; i < edgev.size(); i++) {
            for (int j = i + 1; j < edgev.size(); j++) {
                if (edgecross(i, j)) {
                    sum = sum + delta4;
                }
            }
        }
        return sum;
    }

    private double edgelength() {
        double sum = 0.0;
        for (int i = 0; i < edgev.size(); i++) {
            double x = getBeginNode(i).getEast();
            double y = getBeginNode(i).getSouth();
            double x2 = getEndNode(i).getEast();
            double y2 = getEndNode(i).getSouth();
            double dist = getdist(x, y, x2, y2) / maxdist;
            sum = sum + delta3 * dist * dist;
        }
        return sum;
    }

    private NodeFigure getBeginNode(int edgeIdx) {
        /*
         * PresentationEdge edge=getEdge(edgeIdx); java.util.Iterator
         * it=edge.iteratorEndpoint(); return (PresentationNode)it.next();
         */
        EdgeFigure edge = getEdge(edgeIdx);
        return (NodeFigure) edge.startFigure();
    }

    private double getdist(double x1, double y1, double x2, double y2) {
        double d1 = x1 - x2;
        double d2 = y1 - y2;
        return Math.sqrt(d1 * d1 + d2 * d2);
    }

    private EdgeFigure getEdge(int i) {
        return edgev.get(i);
    }

    private NodeFigure getEndNode(int edgeIdx) {
        /*
         * PresentationEdge edge=getEdge(edgeIdx); java.util.Iterator
         * it=edge.iteratorEndpoint(); it.next(); // skip begin node return
         * (PresentationNode)it.next();
         */
        EdgeFigure edge = getEdge(edgeIdx);
        return (NodeFigure) edge.endFigure();
    }

    private NodeFigure getNode(int i) {
        return nodev.get(i);
    }

    private void layout(java.util.List<NodeFigure> nodev, java.util.List<EdgeFigure> edgev, double min_x1, double min_y1, double max_x1, double max_y1) {
        Layout utility = new Layout();
        utility.nodev = nodev;
        utility.edgev = edgev;
        utility.min_x = min_x1;
        utility.min_y = min_y1;
        utility.max_x = max_x1;
        utility.max_y = max_y1;
        // double cx=(max_x-min_x)/2.0;
        // double cy=(max_y-min_y)/2.0;
        // center all nodes
        // for(tabi = tabv.begin(); tabi != tabv.end(); tabi++){
        // vistab.x=cx;
        // vistab.y=cy;
        // }
        utility.doit();
    }

    private void moveback() {
        int i = oldpos_i;
        getNode(i).setEast((int) (oldpos_x));
        getNode(i).setSouth((int) (oldpos_y));
    }

    private void neighborhood(double radius) {
        int new_x;
        int new_y;
        NodeFigure node = null;
        do {
            int i;
            double alpha;
            do {
                i = random(0, nodev.size() - 1);
                alpha = random(0, 7) * 50.0 / 400.0 * 2.0 * Math.PI;
                oldpos_i = i;
                node = getNode(i);
            } while (!node.isMoveable());
            oldpos_x = node.getEast();
            oldpos_y = node.getSouth();
            new_x = ((int) (node.getEast() + radius * Math.cos(alpha)));
            new_y = ((int) (node.getSouth() + radius * Math.sin(alpha)));
            if (new_x < min_x) {
                new_x = ((int) (min_x));
            }
            if (new_y < min_y) {
                new_y = ((int) (min_y));
            }
            if (new_x > max_x) {
                new_x = ((int) (max_x));
            }
            if (new_y > max_y) {
                new_y = ((int) (max_y));
            }
        } while (new_x == oldpos_x && new_y == oldpos_y);
        node.setEast(new_x);
        node.setSouth(new_y);
    }

    private double nodeDistribution() {
        double sum = 0.0;
        for (int i = 0; i < nodev.size(); i++) {
            double x = getNode(i).getEast();
            double y = getNode(i).getSouth();
            for (int j = 0; j < nodev.size(); j++) {
                if (i != j) {
                    double x2 = getNode(j).getEast();
                    double y2 = getNode(j).getSouth();
                    double dist = getdist(x, y, x2, y2);
                    if (dist != 0.0) {
                        sum = sum + delta1 / dist / dist;
                    } else {
                        sum = sum + delta1;
                    }
                }
            }
        }
        return sum;
    }

    /*
        private double nodeEdgeDistances() {
        double sum = 0;
        for (int j = 0; j < nodev.size(); j++) {
            NodeFigure node = getNode(j);
            for (int i = 0; i < edgev.size(); i++) {
            NodeFigure begNode = getBeginNode(i);
            if (begNode == node)
                continue;
            NodeFigure endNode = getEndNode(i);
            if (endNode == node)
                continue;
            double dist = pt2line(node.getEast(), node.getSouth(), begNode.getEast(), begNode.getSouth(), endNode.getEast(), endNode.getSouth());
            sum = sum + delta4 / dist / dist;
            }
        }
        return sum;
        }

        private double pt2line(double x1, double y1, double xa, double ya, double xb, double yb) {
        double dx = xb - xa;
        if (dx == 0) {
            return Math.abs(x1 - xb);
        }
        double m = (yb - ya) / dx;
        if (m == 0) {
            return Math.abs(y1 - yb);
        }
        double xs = (y1 - ya + m * xa + x1 / m) / (m + 1 / m);
        double ys = m * (xs - xa) + ya;
        return getdist(x1, y1, xs, ys);
        }
    */
    private int random(int lb, int ub) {
        double d = ub - lb + 1;
        return (int) (Math.random() * d) + lb;
    }
}