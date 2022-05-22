package org.tcotool.tools;
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

import ch.softenvironment.jomm.mvc.model.DbCodeType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tcotool.model.CostDriver;
import org.tcotool.model.FactCost;
import org.tcotool.model.Occurance;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Service;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Utility to walk a (Sub)-tree within a TcoModel configuration to search for Code-References.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class CodeRefBuilder implements TreeToolListener {

    private DbCodeType code = null;
    private final List<TcoObject> hits = new ArrayList<>();

    public CodeRefBuilder(TcoModel model, DbCodeType code) {
        super();
        if (code != null) {
            this.code = code;
            TreeTool tool = new TreeTool(this);
            tool.walkTree(model);
        }
    }

    @Override
    public void treatTcoPackage(TcoPackage group) {
        // no code references possible here
    }

    @Override
    public void treatService(Service service) {
        if (code.equals(service.getCategory())
            || code.equals(service.getResponsibility())
            || code.equals(service.getCostCentre())) {
            hits.add(service);
        }
    }

    @Override
    public void treatCostDriver(CostDriver driver) {
        if (code.equals(driver.getCycle())
            || code.equals(driver.getPhase())
            || code.equals(driver.getProcess())) {
            hits.add(driver);
        }
        Iterator<Occurance> it = driver.getOccurrance().iterator();
        while (it.hasNext()) {
            if (code.equals(it.next().getSite())) {
                hits.add(driver);
            }
        }
    }

    @Override
    public void treatFactCost(FactCost cost) {
        if (code.equals(cost.getCatalogue())
            || code.equals(cost.getCause())) {
            hits.add(cost);
        }
    }

    @Override
    public void treatPersonalCost(PersonalCost cost) {
        if (code.equals(cost.getRole())
            || code.equals(cost.getActivity())
            || code.equals(cost.getCause())) {
            hits.add(cost);
        }
    }

    public List<TcoObject> getHits() {
        return hits;
    }
}
