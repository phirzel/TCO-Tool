package org.tcotool;
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

import ch.softenvironment.jomm.DbObjectServer;

/**
 * Registry of all persistent Classes for JOMM.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public abstract class TcotoolUtility {

    public static void registerClasses(DbObjectServer server) {
        // Basket MODEL
        server.register(org.tcotool.model.TcoObject.class, "tcotool.model.TcoObject");
        server.register(org.tcotool.model.SystemParameter.class, "tcotool.model.SystemParameter");
        server.register(org.tcotool.model.Cost.class, "tcotool.model.Cost");
        server.register(org.tcotool.model.FactCost.class, "tcotool.model.FactCost");
        server.register(org.tcotool.model.ConfigurationTag.class, "tcotool.model.ImportReference");
        server.register(org.tcotool.model.Service.class, "tcotool.model.Service");
        server.register(org.tcotool.model.Dependency.class, "tcotool.model.Dependency");
        server.register(org.tcotool.model.TcoPackage.class, "tcotool.model.TcoPackage");
        server.register(org.tcotool.model.TcoModel.class, "tcotool.model.TcoModel");
        server.register(org.tcotool.model.CostDriver.class, "tcotool.model.CostDriver");
        server.register(org.tcotool.model.PersonalCost.class, "tcotool.model.PersonalCost");
        server.register(org.tcotool.model.Site.class, "tcotool.model.Site");
        server.register(org.tcotool.model.Currency.class, "tcotool.model.Currency");
        server.register(org.tcotool.model.CostCause.class, "tcotool.model.CostCause");
        server.register(org.tcotool.model.CostExponent.class, "tcotool.model.CostExponent");
        server.register(org.tcotool.model.CostCentre.class, "tcotool.model.CostCentre");
        server.register(org.tcotool.model.LifeCycle.class, "tcotool.model.LifeCycle");
        server.register(org.tcotool.model.ProjectPhase.class, "tcotool.model.ProjectPhase");
        server.register(org.tcotool.model.ServiceCategory.class, "tcotool.model.ServiceCategory");
        server.register(org.tcotool.model.Activity.class, "tcotool.model.Activity");
        server.register(org.tcotool.model.Role.class, "tcotool.model.Role");
        server.register(org.tcotool.model.Catalogue.class, "tcotool.model.Catalogue");
        server.register(org.tcotool.model.Process.class, "tcotool.model.Process");
        server.register(org.tcotool.model.SupplierInfluence.class, "tcotool.model.SupplierInfluence");
        server.register(org.tcotool.model.Responsibility.class, "tcotool.model.Responsibility");
        server.register(org.tcotool.model.Occurance.class, "tcotool.model.Occurance");
        server.register(org.tcotool.model.Branch.class, "tcotool.model.Branch");
        server.register(org.tcotool.model.Course.class, "tcotool.model.Course");

        // Basket PRESENTATION
        server.register(org.tcotool.presentation.PresentationNode.class, "tcotool.presentation.PresentationNode");
        //  server.register(org.tcotool.presentation.EdgeEnd.class,"tcotool.presentation.EdgeEnd");
        //  server.register(org.tcotool.presentation.PresentationElement.class,"tcotool.presentation.PresentationElement");
        server.register(org.tcotool.presentation.Diagram.class, "tcotool.presentation.Diagram");
        server.register(org.tcotool.presentation.PresentationEdge.class, "tcotool.presentation.PresentationEdge");
        server.register(org.tcotool.presentation.WayPoint.class, "tcotool.presentation.WayPoint");
    }
}