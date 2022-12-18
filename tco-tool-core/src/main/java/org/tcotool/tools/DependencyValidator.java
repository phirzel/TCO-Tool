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

import ch.softenvironment.client.ResourceManager;
import de.normalisiert.utils.graphs.ElementaryCyclesSearch;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.tcotool.model.Dependency;
import org.tcotool.model.Service;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Utility to validate any Dependency in the model.
 *
 * @author Peter Hirzel
 */
public final class DependencyValidator {

	private DependencyValidator() {
		throw new IllegalStateException("utility class");
	}

	/**
	 * Before creating a new Dependency it should be validated, otherwise unwanted failures might result, such as a recursive StackOverflow.
	 * <p>
	 * The client and supplier parameters do not yet exist as a Dependency in the model, therefore it rather a pro-active check, weather it might be established after validation.
	 *
	 * @param client
	 * @param supplier
	 * @return A validation fault or null if client may depend on supplier.
	 */
	public static String validate(ModelUtility utility, Service client, TcoObject supplier) {
		if (client == null) {
			throw new IllegalArgumentException("client must not be null");
		}
		if (supplier == null) {
			throw new IllegalArgumentException("supplier must not be null");
		}
		// TODO NLS
		if (!((supplier instanceof Service) || (supplier instanceof TcoPackage))) {
			return "Supplier of a Dependency must be a Group or Service";
		}
		if (supplier instanceof TcoModel) {
			return ResourceManager.getResource(DependencyValidator.class, "CWDependencyModel");
		}
		if (client.equals(supplier)) {
			return ResourceManager.getResource(DependencyValidator.class, "CWDependencySelf");
		}

		// check for owning Package
		Object parent = client;
		do {
			parent = utility.findParent(parent);
			if (supplier.equals(parent)) {
				return ResourceManager.getResource(DependencyValidator.class, "CWDependencyParent");
			}
		} while (parent != null);

		Iterator<Dependency> iterator = utility.findDependencies(supplier).iterator();
		while (iterator.hasNext()) {
			Dependency dependency = iterator.next();
			if (dependency.getClientId().equals(client.getId()) && dependency.getSupplierId().equals(supplier.getId())) {
				return ResourceManager.getResource(DependencyValidator.class, "CWDependencyDuplicate");
			}
			if (dependency.getClientId().equals(supplier.getId()) && dependency.getSupplierId().equals(client.getId())) {
				// circular dependency: direct => would be determined also by
				// #checkCyclicDependency() too
				return ResourceManager.getResource(DependencyValidator.class, "CWDependencyCircular");
			}
		}

		return validateCyclicDependency(utility, client, supplier);
	}

	/**
	 * Check weather given client and supplier would result in a cyclic dependency.
	 *
	 * @return null if ok otherwise cyclic-fault message
	 * see de.normalisiert.utils.graphs.*
	 * see http://stackoverflow.com/questions/546655/finding-all-cycles-in-graph
	 * see http://dutta.csc.ncsu.edu/csc791_spring07/wrap/circuits_johnson.pdf
	 */
	private static String validateCyclicDependency(ModelUtility utility, Service client, TcoObject supplier) {
		// TcoObject.Id, Node-Count starting at 0
		Map<Long, Long> clientOrSupplierNodes = new HashMap<>();
		Set<Dependency> dependencies = utility.findDependencies((TcoObject) utility.getRoot());
		Iterator<Dependency> iterator = dependencies.iterator();

		int count = 0;
		while (iterator.hasNext()) {
			// build Node-Set
			Dependency dep = iterator.next();
			if (!clientOrSupplierNodes.containsKey(dep.getClientId())) {
				clientOrSupplierNodes.put(dep.getClientId(), Long.valueOf(count++));
			}
			if (!clientOrSupplierNodes.containsKey(dep.getSupplierId())) {
				clientOrSupplierNodes.put(dep.getSupplierId(), Long.valueOf(count++));
			}
		}
		// !!! try add the "new" dependency Nodes !!!
		if (!clientOrSupplierNodes.containsKey(client.getId())) {
			clientOrSupplierNodes.put(client.getId(), Long.valueOf(count++));
		}
		if (!clientOrSupplierNodes.containsKey(supplier.getId())) {
			clientOrSupplierNodes.put(supplier.getId(), Long.valueOf(count++));
		}

		// nodes[Client/SupplierNode-indices/count] => Service or TcoPackage (either Client and/or Supplier)
		Long[] nodes = new Long[clientOrSupplierNodes.size()];
		Iterator<Long> itNodes = clientOrSupplierNodes.values().iterator();
		count = 0;
		while (itNodes.hasNext()) {
			nodes[count++] = itNodes.next();
		}

		// adjMatrix[ClientNode-indices/count][SupplierNode-indices/count]
		boolean[][] adjMatrix = new boolean[clientOrSupplierNodes.size()][clientOrSupplierNodes.size()];
		iterator = dependencies.iterator();
		while (iterator.hasNext()) {
			Dependency cur = iterator.next();
			adjMatrix[clientOrSupplierNodes.get(cur.getClientId()).intValue()][clientOrSupplierNodes.get(cur.getSupplierId()).intValue()] = true;
		}
		// !!! try add the "new" dependency !!!
		adjMatrix[clientOrSupplierNodes.get(client.getId()).intValue()][clientOrSupplierNodes.get(supplier.getId()).intValue()] = true;

		ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(adjMatrix, nodes);
		List cycles = ecs.getElementaryCycles();
		if (cycles.size() > 0) {
			// list bad circular cycles ONLY
			/*
			 * <pre> String cyclePaths = ""; for (int i = 0; i < cycles.size(); i++) { List cycle = (List) cycles.get(i); for (int j = 0; j < cycle.size(); j++)
			 * { Long nodeCount = (Long) cycle.get(j); if (j < cycle.size() - 1) { cyclePaths = cyclePaths + nodeCount + " -> "; } else { cyclePaths =
			 * cyclePaths + nodeCount; } } cyclePaths = cyclePaths + "\n"; } </pre>
			 */
			return ResourceManager.getResource(DependencyValidator.class, "CWDependencyCircular");
		}

		return null;
	}
}
