package org.tcotool.standard.drawing;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.view.ColorChooserDialog;
import ch.softenvironment.view.CommonUserAccess;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import lombok.extern.slf4j.Slf4j;
import org.jhotdraw.contrib.GraphicalCompositeFigure;
import org.jhotdraw.figures.AttributeFigure;
import org.jhotdraw.figures.TextFigure;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureAttributeConstant;
import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.standard.NullHandle;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.util.CollectionsFactory;
import org.tcotool.application.LauncherView;
import org.tcotool.application.ServiceDetailView;
import org.tcotool.model.TcoObject;
import org.tcotool.presentation.PresentationElement;
import org.tcotool.presentation.PresentationNode;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.Formatter;
import org.tcotool.tools.ModelUtility;

/**
 * Figure Specification for all Elements treated as Nodes (analog a Class Symbol in an UML-Class-Diagram).
 *
 * @author Peter Hirzel
 * @see EdgeFigure
 */

@Slf4j
abstract class NodeFigure extends GraphicalCompositeFigure {

	private DependencyView view = null;
	private PresentationNode node = null; // persistent
	private final java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());

	// private double totalCost = -1;

	// composite for Service or Group
	protected TextFigure nameFigure = null;

	// cost-value figures, @see #updateView
	protected TextFigure factCostFigure = null;
	protected TextFigure personalCostFigure = null;
	protected TextFigure dependencyCostFigure = null;
	protected TextFigure totalCostFigure = null;

	/**
	 * NodeFigure constructor.
	 *
	 * @param newPresentationFigure ch.ifa.draw.framework.Figure
	 */
	public NodeFigure(DependencyView view, Figure newPresentationFigure, Object element) throws Exception {
		super(newPresentationFigure);
		this.view = view;

		// modelElement = element;
		Iterator<PresentationElement> iterator = view.getDiagram().getPresentationElement().iterator();
		while (iterator.hasNext()) {
			PresentationElement tmp = iterator.next();
			if (tmp.getSubject() == null) {
				throw new DeveloperException("XML does not contain Subject-REF or reading failure");
			}
			if (tmp.getSubject().equals(element)) {
				node = (PresentationNode) tmp;
				break;
			}
		}
		if (node == null) {
			// not yet displayed
			node = (PresentationNode) ModelUtility.createDbObject(((TcoObject) element).getObjectServer(), PresentationNode.class);
			node.setSubject(element);
		}
	}

	/**
	 * Configure a given PopupMenu. Overwrite this method for specific Menu's.
	 *
	 * @return JPopupMenu
	 */
	protected JPopupMenu adaptPopupMenu(javax.swing.JPopupMenu popupMenu) {
		popupMenu.add(new AbstractAction(ResourceManager.getResource(NodeFigure.class, "MniSelectInNavTree_text") /* SELECT_IN_BROWSER */) {
			@Override
			public void actionPerformed(ActionEvent event) {
				selectInBrowser();
			}
		});

		// popupMenu.add(new JSeparator());
		addFormatMenu(popupMenu);

		popupMenu.setLightWeightPopupEnabled(true);
		return popupMenu;
	}

	/**
	 * Add a Format sub-menu to a PopupMenu.
	 *
	 * @return newly created pop-up menu
	 * @see EdgeFigure
	 */
	protected void addFormatMenu(javax.swing.JPopupMenu popupMenu) {
		JMenu formatMenu = new JMenu(CommonUserAccess.getMnuFormatText());
		/*
		 * formatMenu.add(new
		 * AbstractAction(CommonUserAccess.getMniFormatLineColorText()) { public
		 * void actionPerformed(ActionEvent event) { mniLineColor(); } });
		 */
		formatMenu.add(new AbstractAction(CommonUserAccess.getMniFormatFillColorText()) {
			@Override
			public void actionPerformed(ActionEvent event) {
				mniFillColor();
			}
		});

		popupMenu.add(formatMenu);
	}

	/**
	 * Keep presentation model coordinates.
	 * <p>
	 * public void basicDisplayBox(Point origin, Point corner) { super.basicDisplayBox(origin, corner);
	 * <p>
	 * if ((node.getEast() == null) || (node.getEast().longValue() != origin.x)) { node.setEast(Long.valueOf((long)origin.x)); } if ((node.getSouth() == null) || (node.getSouth().longValue() !=
	 * origin.y)) { node.setSouth(new Long((long)origin.y)); } }
	 */
	@Override
	protected void basicMoveBy(int dx, int dy) {
		super.basicMoveBy(dx, dy);

		node.setEast(Long.valueOf((node.getEast() == null ? 0 : node.getEast().longValue()) + dx));
		node.setSouth(Long.valueOf((node.getSouth() == null ? 0 : node.getSouth().longValue()) + dy));

		LauncherView.getInstance().setModelChanged(true);
	}

	private TextFigure createCostFigure() {
		TextFigure figure = new TextFigure() {
			@Override
			public void setText(String newText) {
				super.setText(newText);
				// NYI updateName(newText);
				update();
			}
		};
		figure.setFont(getFont());
		// packagePathFigure.setText("(from..)");
		figure.setReadOnly(true);

		return figure;
	}

	protected void defineComposite(int gap) {
		// start with an empty Composite
		removeAll();

		// create a TextFigure responsible for the Package-Name
		nameFigure = new TextFigure() {
			@Override
			public void setText(String newText) {
				if (updateName(newText)) {
					super.setText(newText);
				}
				update();
			}
		};
		Font font = getFont();
		font = new Font(font.getName(), Font.BOLD, font.getSize());
		nameFigure.setFont(font);

		// create a figure responsible for the Costs
		factCostFigure = createCostFigure();
		personalCostFigure = createCostFigure();
		dependencyCostFigure = createCostFigure();
		totalCostFigure = createCostFigure();
		font = getFont();
		font = new Font(font.getName(), Font.ITALIC, font.getSize());
		totalCostFigure.setFont(font);

		// add the TextFigure's to the Composite
		GraphicalCompositeFigure composite = new GraphicalCompositeFigure();
		composite.add(nameFigure);
		// composite.add(new SeparatorFigure());
		composite.add(factCostFigure);
		composite.add(personalCostFigure);
		composite.add(dependencyCostFigure);
		composite.add(totalCostFigure);
		composite.getLayouter().setInsets(new Insets(gap, 4, 0, 0));
		add(composite);
	}

	/**
	 * Represent this Figure with an UML-Package Symbol.
	 */
	@Override
	public void draw(Graphics g) {
		// show a package
		g.setColor((Color) getAttribute(FigureAttributeConstant.FILL_COLOR));
		g.setColor((Color) getAttribute(FigureAttributeConstant.FRAME_COLOR));
		getPresentationFigure().draw(g);
		nameFigure.draw(g);
		factCostFigure.draw(g);
		personalCostFigure.draw(g);
		dependencyCostFigure.draw(g);
		totalCostFigure.draw(g);

		// super.draw(g);
	}

	@Override
	public Object getAttribute(FigureAttributeConstant constant) {
		if (constant.equals(FigureAttributeConstant.POPUP_MENU)) {
			// necessary because CheckBox-Selection-State will not be handled
			// properly otherwise
			// @see #addSpecialMenu(JPopupMenu)
			return adaptPopupMenu(new JPopupMenu());
		} else {
			return super.getAttribute(constant);
		}
	}

	/*
	 * @Deprecated protected abstract Color getDefaultFillColor();
	 */

	/**
	 * Return the DependencyDiagram where this Figure is shown.
	 */
	private DependencyView getDiagram() {
		return view;
	}

	public final int getEast() {
		return (int) getPresentationFigure().displayBox().getX();
	}

	/**
	 * Return the nodes Fill-Color.
	 *
	 * @see AttributeFigure#getFillColor()
	 * @see EdgeFigure
	 */
	protected java.awt.Color getFillColor() {
		java.awt.Color color = (java.awt.Color) getAttribute(FigureAttributeConstant.FILL_COLOR);
		if (color == null) {
			return Color.white;
		} else {
			return color;
		}
	}

	/**
	 * Return the name of the Font.
	 */
	protected java.awt.Font getFont() {
		String font = (String) getAttribute(FigureAttributeConstant.FONT_NAME);
		if (font == null) {
			return null;
		} else {
			return java.awt.Font.decode(font);
		}
	}

	/**
	 * Return the drawing color for the Frame.
	 *
	 * @see EdgeFigure
	 */
	protected java.awt.Color getLineColor() {
		java.awt.Color color = (java.awt.Color) getAttribute(FigureAttributeConstant.FRAME_COLOR);
		if (color == null) {
			return Color.black;
		} else {
			return color;
		}
	}

	/**
	 * Return the ModelElement displayed by this Figure.
	 *
	 * @return ModelElement
	 */
	public final Object getModelElement() {
		return node.getSubject();
	}

	/**
	 * Return the model of PresentationElement.
	 *
	 * @return
	 */
	protected final PresentationNode getNode() {
		return node;
	}

	public final int getSouth() {
		return (int) getPresentationFigure().displayBox().getY();
	}

	/**
	 * Called whenever the part throws an exception.
	 *
	 * @param exception
	 */
	protected void handleException(Exception exception) {
		try {
			log.warn("Developer warning", exception);
			LauncherView.getInstance().handleException(exception);
		} finally {
			// just make sure this won't fail any further
		}
	}

	/**
	 * Return default handles on all four edges for this figure.
	 */
	@Override
	public HandleEnumeration handles() {
		// Vector handles = new Vector();
		java.util.List<NullHandle> handles = CollectionsFactory.current().createList();
		// BoxHandleKit.addHandles(this, handles);

		// return getPresentationFigure().handles();
		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new NullHandle(this, RelativeLocator.southWest()));
		handles.add(new NullHandle(this, RelativeLocator.southEast()));

		// return handles;
		return new HandleEnumerator(handles);
	}

	/**
	 * Initialize the node.
	 */
	@Override
	protected void initialize() {
		super.initialize();

		// setLayouter(new SimpleLayouter(this));

		setAttribute(FigureAttributeConstant.POPUP_MENU, adaptPopupMenu(new JPopupMenu()));
		setAttribute(FigureAttributeConstant.FILL_COLOR, Color.white);
		setAttribute(FigureAttributeConstant.FRAME_COLOR, Color.black);
	}

	/**
	 * NodeFigure's are moveable by default.
	 *
	 * @return
	 */
	public final boolean isMoveable() {
		return true;
	}

	/**
	 * Figure's FillColor Action.
	 */
	private void mniFillColor() {
		ColorChooserDialog dialog = new ColorChooserDialog(LauncherView.getInstance(), true);
		if (dialog.isSaved()) {
			setFillColor(dialog.getChosenColor());
			// getClassDiagram().repaint();
		}
	}

	/**
	 * Line-Color Action.
	 * <p>
	 * see #addEditMenu(..)
	 */
	@Deprecated
	private void mniLineColor() {
		ColorChooserDialog dialog = new ColorChooserDialog(LauncherView.getInstance(), true);
		if (dialog.isSaved()) {
			setLineColor(dialog.getChosenColor());
			// getClassDiagram().repaint();
		}
	}

	/**
	 * Remove the Figure visually ONLY. Still kept in real model.
	 */
	public void removeVisually() {
		try {
			// 1) remove drawing
			getDiagram().remove(this);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Select the ModelElement of this Figure in NavigationTree.
	 */
	public void selectInBrowser() {
		LauncherView.getInstance().getPnlNavigation().selectElement(getModelElement());
	}

	/**
	 * Set new east-Position for Figure.
	 *
	 * @param value
	 */
	public final void setEast(int value) {
		if (value != getEast()) {
			// Tracer.getInstance().debug(((TcoObject)getModelElement()).getName()
			// + "->NEW east=" + value);
			Rectangle rectangle = getPresentationFigure().displayBox();
			getPresentationFigure().displayBox(new Rectangle(value, (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight()));
			// if ((node.getEast() == null) || (node.getEast().longValue() !=
			// value)) {
			node.setEast(Long.valueOf(value));
			// }
			updateView();
		}
	}

	/**
	 * Set the Fill Color.
	 *
	 * @see EdgeFigure
	 * @see #setAttribute(FigureAttributeConstant, Object)
	 */
	protected void setFillColor(java.awt.Color color) {
		setAttribute(FigureAttributeConstant.FILL_COLOR, color);

		getNode().setBackground(Long.valueOf(color.getRGB()));

		updateView();
	}

	/**
	 * Set the Line/Frame Color.
	 *
	 * @see EdgeFigure
	 * @see #setAttribute(FigureAttributeConstant, Object)
	 */
	protected void setLineColor(java.awt.Color color) {
		setAttribute(FigureAttributeConstant.FRAME_COLOR, color);
		/*
		 * if ((getNode() != null) &&
		 * (!ColorConverter.isSame(getNode().getForeground(), color))){ //
		 * prevent ping-pong with MetaModelChange
		 * getNode().setForeground(ColorConverter.createColor(color)); }
		 */
	}

	/**
	 * Set new south-Position for Figure.
	 *
	 * @param value
	 */
	public final void setSouth(int value) {
		if (value != getSouth()) {
			// Tracer.getInstance().debug(((TcoObject)getModelElement()).getName()
			// + "->NEW south=" + value);
			Rectangle rectangle = getPresentationFigure().displayBox();
			getPresentationFigure().displayBox(new Rectangle((int) rectangle.getX(), value, (int) rectangle.getWidth(), (int) rectangle.getHeight()));
			// if ((node.getSouth() == null) || (node.getSouth().longValue() !=
			// origin.y)) {
			node.setSouth(Long.valueOf(value));
			// }
			updateView();
		}
	}

	/**
	 * Update Attribute name of ModelElement.
	 */
	protected boolean updateName(String newName) {
		if (!newName.equals(((TcoObject) getModelElement()).getName())) {
			// prevent Ping-Pong with MetaModelChange-Event
			((TcoObject) getModelElement()).setName(newName);
		}

		return true;
	}

	/**
	 * Refresh the view respectively this Figure within the view.
	 */
	public void updateView() {
		if (getModelElement() != null) {
			// node might have changed
			nameFigure.setText(((TcoObject) getModelElement()).getName());

			double temp = Calculator.getValue(view.getCalculator().getTotalCosts((TcoObject) getModelElement(), Calculator.KIND_FC), view.getYear());
			factCostFigure.setText(ResourceManager.getResource(Formatter.class, "CIFactCostAbbreviation") + "  = " + af.format(temp));
			double totalCost = temp;

			temp = Calculator.getValue(view.getCalculator().getTotalCosts((TcoObject) getModelElement(), Calculator.KIND_PC), view.getYear());
			personalCostFigure.setText(ResourceManager.getResource(Formatter.class, "CIPersonalCostAbbreviation") + "  = " + af.format(temp));
			totalCost = totalCost + temp;

			temp = Calculator.getValue(view.getCalculator().calculateDependency((TcoObject) getModelElement()), view.getYear());
			dependencyCostFigure.setText("--> = " + af.format(temp));

			// overall sum again
			totalCost = totalCost + temp;
			totalCostFigure.setText(ch.softenvironment.client.ResourceManager.getResource(ServiceDetailView.class, "TbcTotal_text") + " = "
				+ af.format(totalCost));
		}
	}
}
