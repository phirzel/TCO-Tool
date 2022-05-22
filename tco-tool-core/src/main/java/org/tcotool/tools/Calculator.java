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

import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import ch.softenvironment.util.UserException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.tcotool.model.Cost;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostDriver;
import org.tcotool.model.Dependency;
import org.tcotool.model.FactCost;
import org.tcotool.model.Occurance;
import org.tcotool.model.PersonalCost;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Role;
import org.tcotool.model.Service;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;

/**
 * Generic calculator for configuration of the TCO-Tool.
 *
 * <code>
 * Costs-Map structure:
 * <p>
 * Map-Key | Values ------------------------------------------- Service#getId() | [PersonalCost_Total; FactCost_Total; Year1_Total; ..YearN_Total]
 * </code>
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public abstract class Calculator {

    // kind of cost-relevance
    public static final String KIND_FC = "FC_";
    public static final String KIND_PC = "PC_";

    /**
     * Accumulate TCO-costs per Service.
     */
    protected Map<Long, Map<String, List<Double>>> costs = new HashMap<Long, Map<String, List<Double>>>();

    // undefined Codes
    public static final String COST_CAUSE_UNDEFINED = "COST_CAUSE_UNDEFINED";
    public static final String PROCESS_UNDEFINED = "PROCESS_UNDEFINED";
    public static final String CATEGORY_UNDEFINED = "CATEGORY_UNDEFINED";
    public static final String COSTCENTRE_UNDEFINED = "COSTCENTRE_UNDEFINED";
    public static final String RESPONSIBILITY_UNDEFINED = "RESPONSIBILITY_UNDEFINED";
    public static final String SITE_UNDEFINED = "SITE_UNDEFINED";
    public static final String CATALOGUE_UNDEFINED = "CATALOGUE_UNDEFINED";
    public static final String PERSONAL_ROLE_UNDEFINED = "ROLE_UNDEFINED";
    public static final String PERSONAL_ACTIVITY_UNDEFINED = "ACTIVITY_UNDEFINED";

    // pseudo code ESTIMATED related to Cost#estimated
    public static final String ESTIMATED = "ESTIMATED"; // opposite of KNOWN
    public static final String KNOWN = "KNOWN"; // opposite of ESTIMATED

    // pseudo code DIRECT/INDIRECT related to CostCause#direct
    public static final String DIRECT_UNDEFINED = "DIRECT_UNDEFINED";
    public static final String DIRECT = "DIRECT";
    public static final String INDIRECT = "INDIRECT";

    // pseudo code related to PersonalCost
    public static final String PERSONAL_HOURS_INTERNAL = KIND_PC + "HOURS_INTERNAL";
    public static final String PERSONAL_HOURS_EXTERNAL = KIND_PC + "HOURS_EXTERNAL";
    public static final String PERSONAL_COST_INTERNAL_LUMP = KIND_PC + "INTERNAL_LUMP";
    public static final String PERSONAL_COST_EXTERNAL_LUMP = KIND_PC + "EXTERNAL_LUMP";
    public static final String PERSONAL_COST_EXTERNAL = KIND_PC + "EXTERN";
    public static final String PERSONAL_COST_INTERNAL = KIND_PC + "INTERN";

    // calculation constants
    public static final double PERSONAL_HOURS_UNDEFINED = -1.0;

    /**
     * overall cost (independent of TCO-usage or Depreciation-duration years)
     */
    public static final int INDEX_TOTAL = 0;
    protected ModelUtility utility = null;
    protected TcoObject rootObject = null;
    private long maxDuration = 12; // in month
    private boolean baseOffsetInvolved = false;

    private DbCodeType maskCode = null;

    /**
     * Calculate total of Cost including its multitude.
     */
    public static double calculate(Cost cost) {
        double amount = cost.getAmount() == null ? 0.0 : cost.getAmount().doubleValue();
        return amount * ModelUtility.getMultitudeFactor(cost);
    }

    /**
     * Calculate total Cost of a CostDriver
     *
     * @return [ factCosts, personalCosts ]
     */
    public static double[] calculate(CostDriver driver) {
        double fact = 0.0;
        double pa = 0.0;
        double multitude = ModelUtility.getMultitudeFactor(driver);

        java.util.Iterator<Cost> iterator = driver.getCost().iterator();
        while (iterator.hasNext()) {
            Cost cost = iterator.next();
            if (cost instanceof FactCost) {
                fact = fact + calculate(cost);
            } else {
                pa = pa + calculate(cost);
            }
        }

        double[] sums = {fact * multitude, pa * multitude};
        return sums;
    }

    /**
     * Calculate the total Ocurrence's for the given driver.
     */
    public static double calculateOccuranceTotal(CostDriver driver) {
        double sum = 0.0;
        java.util.Iterator<Occurance> iterator = driver.getOccurrance().iterator();
        while (iterator.hasNext()) {
            Occurance occurance = iterator.next();
            if (occurance.getMultitude() != null) {
                sum = sum + occurance.getMultitude().doubleValue();
            }
        }
        return sum;
    }

    /**
     * Calculate hours defined within given PersonalCost. Multitude is not considered at all. Return PERSONAL_HOURS_UNDEFINED (-1.0) if not determinable.
     *
     * @param cost
     * @return double
     */
    public static double calculateHours(PersonalCost cost) {
        if (cost.getHours() != null) {
            return cost.getHours().doubleValue();
        }

        if (cost.getRole() == null) {
            if ((cost.getAmount() != null) && ((cost.getHourlyRate() != null) && (cost.getHourlyRate().doubleValue() != 0.0))) {
                return cost.getAmount().doubleValue() / cost.getHourlyRate().doubleValue();
            }
        } else {
            if (cost.getRole().getYearlyHours() != null) {
                // no hours mean man year is implied
                return cost.getRole().getYearlyHours().doubleValue();
            }
        }

        // hours cannot be calculated, assume their cost as lump
        return PERSONAL_HOURS_UNDEFINED;
    }

    /**
     * Calculate available hours for a Role per year.
     *
     * @param role
     * @return
     */
    public static double calculateAvailableHours(Role role) {
        return role.getYearlyHours().doubleValue() / 100.0 * role.getEmploymentPercentageAvailable().doubleValue();
    }

    /**
     * FTE/YearlyHours. Calculates a rounded(2) value!
     *
     * @param role
     * @return
     */
    public static double calculateHourlyRate(Role role) {
        return AmountFormat.round(role.getFullTimeEquivalent().doubleValue() / role.getYearlyHours().doubleValue());
    }

    /**
     * Calculate open hours for given role, by means non-assigned availability of a Role.
     *
     * @param utility
     * @param role
     * @return
     */
    public static double calculateFreeHours(ModelUtility utility, Role role) {
        double sum = calculateAvailableHours(role);

        CodeRefBuilder builder = new CodeRefBuilder((TcoModel) utility.getRoot(), role);
        Iterator<TcoObject> hits = builder.getHits().iterator();
        while (hits.hasNext()) {
            PersonalCost cost = (PersonalCost) hits.next();
            if (cost.getHours() != null) {
                // concrete hours booked
                double hours = cost.getHours().doubleValue();
                sum = sum - hours * utility.getMultitudeFactor(cost, true);
            } else {
                // FTE => Man-year
                sum = sum - role.getYearlyHours().doubleValue() * utility.getMultitudeFactor(cost, true);
            }
        }
        return sum;
    }

    /**
     * Calculate the cost of a Service without Dependency and without Service multitude.
     *
     * @param service
     * @return [0]=>FactCost]; [1]=>PersonalCost
     */
    public static double[] calculateWithoutFactor(Service service) {
        double[] sums = {0.0, 0.0};
        java.util.Iterator<CostDriver> iterator = service.getDriver().iterator();
        while (iterator.hasNext()) {
            double[] drv = Calculator.calculate(iterator.next());
            // FactCost
            sums[0] += drv[0];
            // PersonalCost
            sums[1] += drv[1];
        }
        /*
         * double factor = ModelUtility.getMultitudeFactor(service); sums[0] *= factor; sums[1] *= factor;
         */
        return sums;
    }

    /**
     * Summarize total (PersonalCost + FactCost + DependencyCost) in tree for given object, including recusive Dependency costs in supplier-chain.
     */
    public double calculateOverallCosts(TcoObject object) {
        return getValue(getTotalCosts(object, KIND_PC), INDEX_TOTAL) + getValue(getTotalCosts(object, KIND_FC), INDEX_TOTAL)
            + getValue(calculateDependency(object), INDEX_TOTAL);
    }

    /**
     * Calculate the Dependency-Costs accumulated for the given clientObject, where own FactCost & PersonalCost of clientObject itself are not included here. Dependencies are contained in
     * supplier-List of clientObject and may be recursive.
     *
     * @return [DependencyTotal(PersonalCost + FactCost); TcoYear1(PersonalCost + FactCost); ..TcoYearN(PersonalCost + FactCost)]
     */
    public List<Double> calculateDependency(TcoObject clientObject) {
        // calculate whole model to make sure supplier costs are defined

        // Calculator calculator = new CalculatorTCO(utility,
        // (TcoPackage)utility.getRoot(), ReportTool.getUsageDuration());

        // iterate over all direct Suppliers
        java.util.Iterator<Dependency> iterator = clientObject.getSupplierId().iterator();
        List<Double> results = new ArrayList<Double>();
        while (iterator.hasNext()) {
            Dependency dependency = iterator.next();
            TcoObject supplier = utility.findSupplier(dependency);
            // calculate costs from supplier
            List<Double> tmp = getTotalCosts(supplier, KIND_PC);
            accumulateLists(tmp, getTotalCosts(supplier, KIND_FC));
            accumulateLists(tmp, calculateDependency(supplier));
            // adapt distribution percentage
            double distribution = dependency.getDistribution().doubleValue() / 100.0;
            for (int i = 0; i < tmp.size(); i++) {
                tmp.set(i, new Double(tmp.get(i).doubleValue() * distribution));
            }
            accumulateLists(results, tmp);
        }
        return results;
    }

    /**
     * Return depth of Supplier-chain in Dependencies.
     *
     * @param utility
     * @param clientObject
     * @param depth
     * @return
     */
    public static int calculateDependencyDepth(ModelUtility utility, TcoObject clientObject, int depth) {
        int localDepth = depth;
        Iterator<Dependency> it = clientObject.getSupplierId().iterator();
        if (it.hasNext()) {
            localDepth++;
        }
        int tmpDepth = 0;
        while (it.hasNext()) {
            Dependency dep = it.next();
            TcoObject supplier = utility.findObject(dep.getSupplierId(), (TcoObject) utility.getRoot());
            int supplierDepth = calculateDependencyDepth(utility, supplier, 0);
            if (supplierDepth > tmpDepth) {
                tmpDepth = supplierDepth;
            }
        }

        return localDepth + tmpDepth;
    }

    private static String createCodeKey(final String kind, Object code) {
        if (code == null) {
            throw new IllegalArgumentException("no key exists for code==null");
        } else if (code instanceof DbObject) {
            return kind + StringUtils.getPureClassName(code.getClass()) + ((DbObject) code).getId().toString();
        } else {
            // <undefined> code
            return kind + code;
        }
    }

    /**
     * Return element at index from list (0.0 if not existing).
     *
     * @param list
     * @param year INDEX_TOTAL, 1, 2,..
     * @return
     */
    public static double getValue(List<Double> list, final int year) {
        if ((list != null) && (list.size() > year)) {
            return list.get(year).doubleValue();
        } else {
            return 0.0;
            // throw new
            // DeveloperException("Calculator was not constructed for this year, therefore no values can be estimated");
        }
    }

    /**
     * Initialize calculator for given rootObject and TotalCosts only.
     * <p>
     * No duration estimations must be calculated with this constructor!
     *
     * @param object
     */
    public Calculator(ModelUtility utility) {
        this(utility, (TcoObject) utility.getRoot(), 0);
    }

    /**
     * @see #Calculator(ModelUtility, TcoObject, long, ServiceCategory, Responsibility)
     */
    public Calculator(ModelUtility utility, TcoObject rootObject, long maxDurationMonths) {
        this(utility, rootObject, maxDurationMonths, null);
    }

    /**
     * Create calculator for a given time period.
     * <p>
     * A filter (maskCode) might be set to only calculate TcoObject's (Service, CostDriver, Cost) where exactly this filter-maskCode is attached, for e.g. if maskCode is a ServiceCatgery-code
     * ("Development"), then only Service's attaching this very code in hierarchy are included (if set only TcoObject's containing this code are to be considered in calculation).
     *
     * @param utility
     * @param rootObject (where to start calculation hierarchically
     * @param maxDuration in months to calculate costs for (either Usage- or Depreciation-Duration)
     * @param maskCode (only calculate TcoObject's where given maskCode is contained in hierarchy (for e.g. service.getCategory()==maskCode)
     * @param responsibility (optionally calculate only services of this Responsibility; null for all)
     * @see #calc(Service, double)
     */
    public Calculator(ModelUtility utility, TcoObject rootObject, long maxDurationMonths, DbCodeType maskCode) {
        super();
        this.utility = utility;
        this.maxDuration = maxDurationMonths;
        this.rootObject = rootObject;
        this.maskCode = maskCode;

        walkPackage(rootObject, 1);
    }

    /**
     * Calculate the costs for all services managed by given object. Recursive Algorithm.
     *
     * @param object (TcoPackage or Service)
     * @param multitude (of owner of given object)
     */
    private void walkPackage(TcoObject object, long multitude) {
        long factor = (long) ModelUtility.getMultitudeFactor(object) * multitude;

        if (object instanceof TcoPackage) {
            Iterator<Service> itService = ((TcoPackage) object).getService().iterator();
            // 1) services within Package
            while (itService.hasNext()) {
                calc(itService.next(), factor);
            }

            // 2) sub-packages
            Iterator<TcoPackage> itGroup = ((TcoPackage) object).getOwnedElement().iterator();
            while (itGroup.hasNext()) {
                walkPackage(itGroup.next(), factor);
            }
        } else if (object instanceof Service) {
            calc((Service) object, multitude);
        } else {
            throw new DeveloperException("TcoObject kind not implemented!");
        }
    }

    /**
     * Accumulate the costs of given service.
     *
     * @param service
     * @param groupFactor Multitude of owning TcoPackage
     */
    private void calc(Service service, double groupFactor) {
        // only calculate if service-code not given or equal to given one
        double serviceFactor = ModelUtility.getMultitudeFactor(service);

        java.util.Iterator<CostDriver> iterator = service.getDriver().iterator();
        while (iterator.hasNext()) {
            org.tcotool.model.CostDriver driver = iterator.next();
            double driverFactor = ModelUtility.getMultitudeFactor(driver);

            // sum all costs for any assigned code assigned on Service,
            // CostDriver or Cost
            Iterator<Cost> costs = driver.getCost().iterator();
            while (costs.hasNext()) {
                Cost cost = costs.next();
                if (cost.getBaseOffset() == null) {
                    Tracer.getInstance().developerWarning("Auto-correction: cost#baseOffset set to 0");
                    cost.setBaseOffset(Long.valueOf(0));
                } else if (cost.getBaseOffset().longValue() > 0) {
                    baseOffsetInvolved = true;
                }
                if ((maskCode != null)
                    && !(maskCode.equals(service.getCategory()) || maskCode.equals(service.getResponsibility()) || maskCode.equals(service.getCostCentre())
                    || maskCode.equals(driver.getCycle()) || maskCode.equals(driver.getPhase()) || maskCode.equals(driver.getProcess())
                    || maskCode.equals(cost.getCause()) || ((cost instanceof PersonalCost) && (maskCode.equals(((PersonalCost) cost).getRole()) || maskCode
                    .equals(((PersonalCost) cost).getActivity()))))) {
                    // exclude TcoObject's not attached to this filter from
                    // calculation
                    continue;
                } else {
                    if (cost.getAmount() == null) {
                        Tracer.getInstance().developerWarning("Auto-correction: cost#amount set to 0.0");
                        cost.setAmount(new Double(0.0));
                    }
                    double costFactor = groupFactor * serviceFactor * driverFactor * ModelUtility.getMultitudeFactor(cost);
                    double totalCost = cost.getAmount().doubleValue() * costFactor;

                    // calculate specific algorithm
                    calculate(service, driver, cost, totalCost);
                }
            }
        }
    }

    /**
     * Return whether one of the calculated Cost's has a baseOffset > 0.
     *
     * @return
     */
    public boolean isBaseOffsetInvolved() {
        return baseOffsetInvolved;
    }

    /**
     * Do the specific calculations according to your algorithm.
     */
    protected abstract void calculate(Service service, CostDriver driver, Cost cost, double totalCost);

    private Map<String, List<Double>> getServiceMap(Service key) {
        if (!costs.containsKey(key.getId())) {
            costs.put(key.getId(), new HashMap<String, List<Double>>());
        }
        return costs.get(key.getId());
    }

    /**
     * Accumulate the given cost among all codes involved.
     *
     * @param year 0=Total at all; 1..n=TCO/Depreciation-distribution
     * @param amount costs to charge for given year
     */
    protected final void accumulateCodes(Service service, CostDriver driver, Cost cost, int year, double amount) {
        String kind = (cost instanceof PersonalCost ? KIND_PC : KIND_FC);
        Map<String, List<Double>> serviceMap = getServiceMap(service);
        // Service codes
        Object key = CATEGORY_UNDEFINED;
        if (service.getCategory() != null) {
            key = service.getCategory();
        }
        storeIntoCodeList(serviceMap, kind, key, year, amount);

        key = RESPONSIBILITY_UNDEFINED;
        if (service.getResponsibility() != null) {
            key = service.getResponsibility();
        }
        storeIntoCodeList(serviceMap, kind, key, year, amount);

        key = COSTCENTRE_UNDEFINED;
        if (service.getCostCentre() != null) {
            key = service.getCostCentre();
        }
        storeIntoCodeList(serviceMap, kind, key, year, amount);

        // CostDriver codes
        key = PROCESS_UNDEFINED;
        if (driver.getProcess() != null) {
            key = driver.getProcess();
        }
        storeIntoCodeList(serviceMap, kind, key, year, amount);
        double maxOccurance = Calculator.calculateOccuranceTotal(driver);
        if (maxOccurance > 0) {
            Iterator<Occurance> itOccurance = driver.getOccurrance().iterator();
            while (itOccurance.hasNext()) {
                Occurance occurance = itOccurance.next();
                if ((occurance.getSite() != null) && (occurance.getMultitude() != null)) {
                    // Formula: Cost per Service / maxOccurance(all Site's) *
                    // occurrence.multitude(this Site)
                    storeIntoCodeList(serviceMap, kind, occurance.getSite(), year, amount / maxOccurance * occurance.getMultitude().doubleValue());
                } else {
                    // should not happen
                    Tracer.getInstance().developerWarning("Occurrence without Site[1] or Multitude[1]");
                }
            }
        } else {
            storeIntoCodeList(serviceMap, kind, SITE_UNDEFINED, year, amount);
        }

        // Cost codes
        key = COST_CAUSE_UNDEFINED;
        if (cost.getCause() != null) {
            key = cost.getCause();
        }

        storeIntoCodeList(serviceMap, kind, key, year, amount);
        // pseudo-code "ESTIMATED/KNOWN"
        // TODO calculate per CostCause by using "key + ESTIMATED/KNOWN"
        if (cost.getEstimated().booleanValue()) {
            storeIntoCodeList(serviceMap, kind, ESTIMATED, year, amount);
        } else {
            storeIntoCodeList(serviceMap, kind, KNOWN, year, amount);
        }
        // pseudo-code "DIRECT/INDIRECT"
        // TODO calculate per CostCause by using "key + DIRECT/INDIRECT"
        if ((cost.getCause() == null) || (cost.getCause().getDirect() == null)) {
            storeIntoCodeList(serviceMap, kind, DIRECT_UNDEFINED, year, amount);
        } else if (cost.getCause().getDirect().booleanValue()) {
            storeIntoCodeList(serviceMap, kind, DIRECT, year, amount);
        } else {
            storeIntoCodeList(serviceMap, kind, INDIRECT, year, amount);
        }
        if (cost instanceof FactCost) {
            key = CATALOGUE_UNDEFINED;
            if (((FactCost) cost).getCatalogue() != null) {
                key = ((FactCost) cost).getCatalogue();
            }
            storeIntoCodeList(serviceMap, kind, key, year, amount);
        } else if (cost instanceof PersonalCost) {
            PersonalCost pCost = (PersonalCost) cost;

            key = PERSONAL_ACTIVITY_UNDEFINED;
            if (pCost.getActivity() != null) {
                key = pCost.getActivity();
            }
            storeIntoCodeList(serviceMap, kind, key, year, amount);

            key = PERSONAL_ROLE_UNDEFINED;
            if (pCost.getRole() != null) {
                key = pCost.getRole();
            }
            storeIntoCodeList(serviceMap, kind, key, year, amount);

            double hours = calculateHours(pCost);
            if (hours == PERSONAL_HOURS_UNDEFINED) {
                // just count amount generally (Lump = "Pauschal" (de))
                if (pCost.getInternal().booleanValue()) {
                    storeIntoCodeList(serviceMap, kind, PERSONAL_COST_INTERNAL_LUMP, year, amount);
                } else {
                    storeIntoCodeList(serviceMap, kind, PERSONAL_COST_EXTERNAL_LUMP, year, amount);
                }
            } else {
                hours *= utility.getMultitudeFactor(pCost, true);
                if (pCost.getInternal().booleanValue()) {
                    storeIntoCodeList(serviceMap, kind, PERSONAL_HOURS_INTERNAL, year, hours);
                } else {
                    storeIntoCodeList(serviceMap, kind, PERSONAL_HOURS_EXTERNAL, year, hours);
                }
            }
        }
    }

    /**
     * Return the List in given map for given key.
     *
     * @param map
     * @param key
     * @return
     */
    private static List<Double> getCodeList(Map<String, List<Double>> map, final String key) {
        if (!map.containsKey(key)) {
            List<Double> codeList = new ArrayList<Double>();
            codeList.add(new Double(0.0)); // [INDEX_TOTAL] initial Total Costs
            map.put(key, codeList);
        }
        return map.get(key);
    }

    /**
     * Store the amount for year and code.
     *
     * @param serviceMap
     * @param key
     * @param year
     * @param amount
     */
    protected final void storeIntoCodeList(Map<String, List<Double>> serviceMap, final String kind, Object code, int year, double amount) {
        if (year < INDEX_TOTAL) {
            Tracer.getInstance().runtimeWarning("some Cost#baseOffset must be unexpectedly negative!");
            return;
        }
        // keep the costs for service, year and code
        List<Double> codeList = getCodeList(serviceMap, createCodeKey(kind, code));
        for (int i = codeList.size(); i <= year; i++) {
            // (first entry in this year) || (Cost#baseOffset > 1 year)
            codeList.add(new Double(0.0));
        }
        codeList.set(year, new Double(codeList.get(year).doubleValue() + amount));
    }

    /**
     * Return total costs for given service and code.
     *
     * @param object null for accumulated costs over all services
     * @param kind
     * @param code DbCode; String for undefined code
     * @return [Total_Kind_Code; Year1_Kind_Code;..YearN_Kind_Code]
     */
    public List<Double> getCosts(TcoObject object, final String kind, Object code) {
        TcoObject costObject = (object == null ? rootObject : object);
        String key = createCodeKey(kind, code);
        if (costObject instanceof Service) {
            return getCodeList(getServiceMap((Service) costObject), key);
        } else if (costObject instanceof TcoPackage) {
            List<Double> results = new ArrayList<Double>();
            // recursively add services and groups
            Iterator<Service> itService = ((TcoPackage) costObject).getService().iterator();
            while (itService.hasNext()) {
                accumulateLists(results, getCosts(itService.next(), kind, code));
            }
            Iterator<TcoPackage> itPackage = ((TcoPackage) costObject).getOwnedElement().iterator();
            while (itPackage.hasNext()) {
                accumulateLists(results, getCosts(itPackage.next(), kind, code));
            }
            return results;
        } else {
            // TODO NYI: "TcoObject: " + object
            return new ArrayList<Double>();
        }
    }

    /**
     * Return total costs for given object.
     *
     * @param object null for accumulated costs over all services
     * @param kind
     * @return [Total_Kind; Year1_Kind;..YearN_Kind]
     */
    public List<Double> getTotalCosts(TcoObject object, final String kind) {
        List<Double> results = new ArrayList<Double>();
        try {
            // use the specific code (CostCause) to calculate sum
            Iterator<? extends DbCodeType> codes = rootObject.getObjectServer().retrieveCodes(CostCause.class).iterator();
            while (codes.hasNext()) {
                accumulateLists(results, getCosts(object, kind, codes.next()));
            }
            accumulateLists(results, getCosts(object, kind, COST_CAUSE_UNDEFINED));
        } catch (Exception e) {
            // TODO NLS
            throw new UserException("CostCause-Code: " + e.getLocalizedMessage(), "Calculation error", e);
        }
        return results;
    }

    /**
     * Add the Double-values in summand to results.
     *
     * @param results
     * @param summand
     */
    public static void accumulateLists(List<Double> results, List<Double> summand) {
        // TODO move to sebase ListUtils
        for (int i = 0; i < summand.size(); i++) {
            if (results.size() > i) {
                results.set(i, new Double(results.get(i).doubleValue() + summand.get(i).doubleValue()));
            } else {
                results.add(summand.get(i));
            }
        }
    }

    /**
     * Return cost-list over given object.
     *
     * @param object null for all services
     * @return [PersonalCosts_Total; FactCosts_Total; Year1(PC + FC);.. YearN(PC + FC)]
     */
    public List<Double> getCostBlock(TcoObject object) {
        TcoObject costObject = (object == null ? rootObject : object);
        List<Double> costs = new ArrayList<Double>();

        List<Double> totalPersonal = getTotalCosts(costObject, KIND_PC);
        List<Double> totalFacts = getTotalCosts(costObject, KIND_FC);
        costs.add(new Double(getValue(totalPersonal, INDEX_TOTAL)));
        costs.add(new Double(getValue(totalFacts, INDEX_TOTAL)));
        for (int i = 0; i < getDurationYears(); i++) {
            // print years
            int index = i + INDEX_TOTAL + 1;
            costs.add(new Double(getValue(totalFacts, index) + getValue(totalPersonal, index)));
        }
        return costs;
    }

    /**
     * Return a Map of given Code values.
     *
     * @param object null for all services
     * @param codes the DbCodeType interesting
     * @return key=CostType#getIliCode(); value=List of values for each year of the same code
     */
    public Map<Long, List<Double>> getCostBlock(TcoObject object, List<? extends DbCodeType> codes) {
        // TcoObject costObject = (object == null ? rootObject : object);
        Map<Long, List<Double>> map = new HashMap<Long, List<Double>>();

        // use the specific code (CostCause) to calculate sum
        Iterator<? extends DbCodeType> iterator = codes.iterator();
        while (iterator.hasNext()) {
            DbCodeType code = iterator.next();
            List<Double> results = new ArrayList<Double>();
            accumulateLists(results, getCosts(object, KIND_PC, code));
            accumulateLists(results, getCosts(object, KIND_FC, code));
            map.put(((DbCode) code).getId(), results);
        }

        return map;
    }

    /**
     * Return total costs for Personal- and FactCosts.
     *
     * @see #getCodeTotal(TcoObject, Object, String)
     */
    public List<Double> getCodeTotal(TcoObject object, Object code) {
        List<Double> results = getCodeTotal(object, code, KIND_FC);
        accumulateLists(results, getCodeTotal(object, code, KIND_PC));
        return results;
    }

    /**
     * Return total for object and code.
     *
     * @param object null for whole configuration
     * @param code DbCodeType
     * @return
     */
    public List<Double> getCodeTotal(TcoObject object, Object code, final String kind) {
        return getCosts(object, kind, code);
    }

    /**
     * @see #Calculator(.., maxDurationMonths)
     */
    public long getMaxDurationMonths() {
        return maxDuration;
    }

    /**
     * Calculate the duration in years, where last partial year counts as one year more. Useful as title-column count.
     */
    public int getDurationYears() {
        int years = (int) getMaxDurationMonths() / 12;
        if (getMaxDurationMonths() % 12 > 0) {
            years++;
        }
        return years;
    }
}