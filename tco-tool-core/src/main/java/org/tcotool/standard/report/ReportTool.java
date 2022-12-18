package org.tcotool.standard.report;

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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.AttributeList;
import ch.softenvironment.jomm.serialize.CsvSerializer;
import ch.softenvironment.jomm.serialize.HtmlSerializer;
import ch.softenvironment.util.AmountFormat;
import ch.softenvironment.util.StringUtils;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.tcotool.application.LauncherView;
import org.tcotool.model.CostCause;
import org.tcotool.model.CostCentre;
import org.tcotool.model.CostExponent;
import org.tcotool.model.Process;
import org.tcotool.model.Responsibility;
import org.tcotool.model.Service;
import org.tcotool.model.ServiceCategory;
import org.tcotool.model.Site;
import org.tcotool.model.SystemParameter;
import org.tcotool.model.TcoModel;
import org.tcotool.model.TcoObject;
import org.tcotool.model.TcoPackage;
import org.tcotool.tools.Calculator;
import org.tcotool.tools.ModelUtility;

/**
 * Reporting Utility to generate different Outputs by TCO-Tool in HTML or CSV. Design Pattern: Visitor
 *
 * @author Peter Hirzel
 */
@Slf4j
public abstract class ReportTool extends ch.softenvironment.jomm.serialize.HtmlSerializer {

	public static final int PERCENTAGE_FRACTION_DIGITS = 1;
	public static final long DEFAULT_DURATION = 48;
	// HTML style @see #getAttributeWidth()
	protected static final String YEAR_COLUMN_WITH_STYLE = "style=" + ATTRIBUTE_WIDTH + ":120px";

	protected ModelUtility utility = null;
	protected Calculator calculator = null;
	protected String title = "";

	// CSV-support
	private static final String UNKNOWN = "<???>";
	// private static final char SEPARATOR = ';';
	private final ch.softenvironment.jomm.serialize.CsvSerializer csvWriter = new ch.softenvironment.jomm.serialize.CsvSerializer(new java.io.StringWriter());

	// TODO nasty ref to LauncherView.getInstance()
	protected java.text.NumberFormat af = AmountFormat.getAmountInstance(LauncherView.getInstance().getSettings().getPlattformLocale());
	protected java.text.NumberFormat hourFormat = null;
	// private AttributeList alignRight = null;
	protected AttributeList abstractCell = new AttributeList(ATTRIBUTE_WIDTH, "130");

	// reporting data
	protected boolean printDetails = false;

	/**
	 * ReportTool constructor comment.
	 *
	 * @param utility
	 * @param title of the Report
	 */
	protected ReportTool(ModelUtility utility, final String title) {
		super(new java.io.StringWriter());

		this.utility = utility;
		this.title = title;

		hourFormat = java.text.NumberFormat.getNumberInstance();
		hourFormat.setMinimumFractionDigits(1);
		hourFormat.setMaximumFractionDigits(1);
		// formatter.setGroupingSize(3); // separate thousand's
		hourFormat.setGroupingUsed(true);
	}

	/**
	 * Scales Amount value acc. to SystemParameter#reportCostExponent in thousand or millions.
	 *
	 * @see #getCostUnit()
	 */
	protected String amount(double value) {
		int exponent = 0;
		try {
			SystemParameter sysParams = utility.getSystemParameter();
			if (DbEnumeration.isIliCode(sysParams.getReportCostExponent(), CostExponent.INTHOUSAND)) {
				exponent = 1;
			} else if (DbEnumeration.isIliCode(sysParams.getReportCostExponent(), CostExponent.INMILLION)) {
				exponent = 2;
			}
		} catch (Exception e) {
			log.warn("INGORED: SystemParameter's FAILURE");
		}
		switch (exponent) {
			case 1: {
				return af.format(value / 1000.0);
			}
			case 2: {
				return af.format(value / 1000000.0);
			}
			default: {
				return af.format(value);
			}
		}
	}

	@Override
	public void breakLine() throws java.io.IOException {
		super.breakLine();

		getCsvWriter().newline();
	}

	protected URL getHeaderImage() {
		return null;
	}

	/**
	 * Display Service-Cost's in detail (recursively). Only consider Services below given root.
	 *
	 * @param root (TcoPackage or Service)
	 * @deprecated (redesigns)
	 */
	protected void costBlockCostCause(TcoObject root) throws Exception {
		printDetails = true;

		startHtml();
		startBody(null);
		// print the model
		String prefix = "1";
		if (root instanceof TcoPackage) {
			walkPackage((org.tcotool.model.TcoPackage) root, prefix, 1);
		} else {
			// Service
			header(prefix, 1, root);
			encodeCostBlock(root, root.getObjectServer().retrieveCodes(CostCause.class), Calculator.COST_CAUSE_UNDEFINED,
				ModelUtility.getTypeString(CostCause.class));
			encodeCodes(root.getObjectServer(), root);
		}
		// encodeTcoCodes(root.getObjectServer(), root);
		endBody();
		endHtml();
	}

	/**
	 * Generic method to encode a block of costs related to given codes.
	 * <p>
	 * If (printDetails==true) then all Service's (either the given root itself or the owned services of a group represented by object) will be printed separately in an own such block.
	 *
	 * @param root TcoPackage or Service
	 * @deprecated (redesign not really useful in a generic way)
	 */
	protected void encodeCostBlock(TcoObject root, List<? extends DbCodeType> codes, final String codeUndefined, final String title) throws IOException {
		// overwrite if nececessary
	}

	/**
	 * Write a text in what unit and currency the listed costs are represented, for e.g. "All costs in [kCHF]; %5.3"
	 *
	 * @param suffix (for e.g. "; %5.3")
	 * @see #getCostUnit()
	 */
	protected void encodeCostUnit(final String suffix) throws java.io.IOException {
		String tmp = getRsc("CICostExponent") + " [";
		italic(tmp + getCostUnit() + "]" + suffix);

		try {
			// in csv costs are always as is (without unit!)
			getCsvWriter().cell(tmp + utility.getSystemParameter().getDefaultCurrency().getNameString() + "]" + suffix);
		} catch (Exception e) {
			log.warn("INGORED: SystemParameters FAILURE: " + e.getLocalizedMessage());
			getCsvWriter().cell(tmp + UNKNOWN + "]" + suffix);
		}
	}

	/**
	 * Print all meaningful DbCodeType's for a CostBlock-Report one after each other.
	 *
	 * @param root null for whole configuration
	 */
	protected void encodeCodes(DbObjectServer server, TcoObject root) throws Exception {
		/*
		 * String tmp = getRsc("CIEstimatedRatio") + " = "; double known = Calculator.getValue(calculator.getCodeTotal(root, Calculator.KNOWN),
		 * Calculator.INDEX_TOTAL); double estimated = Calculator.getValue(calculator.getCodeTotal(root, Calculator.ESTIMATED), Calculator.INDEX_TOTAL);
		 * nativeContent(tmp + amount(estimated) + " : " + amount(known)); getCsvWriter().cell(tmp); getCsvWriter().cell(new Double(estimated));
		 * getCsvWriter().cell(" : "); getCsvWriter().cell(new Double(known)); getCsvWriter().newline();
		 */
		// Cost->CostCause (most important Enumeration)
		List<? extends DbCodeType> codes = server.retrieveCodes(CostCause.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.COST_CAUSE_UNDEFINED, ModelUtility.getTypeString(CostCause.class));
			endParagraph();
		}
		// service Codes
		codes = server.retrieveCodes(ServiceCategory.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.CATEGORY_UNDEFINED, ModelUtility.getTypeString(ServiceCategory.class));
			endParagraph();
		}
		codes = server.retrieveCodes(Responsibility.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.RESPONSIBILITY_UNDEFINED, ModelUtility.getTypeString(Responsibility.class));
			endParagraph();
		}
		codes = server.retrieveCodes(CostCentre.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.COSTCENTRE_UNDEFINED, ModelUtility.getTypeString(CostCentre.class));
			endParagraph();
		}

		// CostDriver Codes
		codes = server.retrieveCodes(org.tcotool.model.Process.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.PROCESS_UNDEFINED, ModelUtility.getTypeString(Process.class));
			endParagraph();
		}
		// TODO NYI: ProjectPhase (only if UserActionRights are available)
		// TODO NYI: LifeCycle (only if UserActionRights are available)
		codes = server.retrieveCodes(org.tcotool.model.Site.class);
		if (codes.size() > 0) {
			startParagraph();
			encodeCostBlock(root, codes, Calculator.SITE_UNDEFINED, ModelUtility.getTypeString(Site.class));
			endParagraph();
		}
		/*
		 * no sense // other Cost codes (CostType see above) codes = server.retrieveCodes(Catalogue.class); if (codes.size() > 0) { startParagraph();
		 * encodeCostBlock(root, codes, Calculator.CATALOGUE_UNDEFINED, ModelUtility.getTypeString(Catalogue.class)); endParagraph(); }
		 */
	}

	@Override
	public void endParagraph() throws IOException {
		super.endParagraph();

		getCsvWriter().newline();
	}

	@Override
	public void endTable() throws java.io.IOException {
		super.endTable();

		getCsvWriter().newline();
	}

	/**
	 * Force a NewLine only at EndRow in CSV => not at #startTableRow())
	 */
	@Override
	public void endTableRow() throws java.io.IOException {
		super.endTableRow();

		getCsvWriter().newline();
	}

	/**
	 * (irrelevant for CSV)
	 *
	 * @see #startTreeHeader(int)
	 */
	protected void endTreeHeader(int level) throws IOException {
		if (level > 1) {
			endElement("td");
			endTableRow();
			endTable();
			// horizontalRule();
		}
	}

	/**
	 * Return a fixed table HTML attribute.
	 */
	protected ch.softenvironment.jomm.serialize.AttributeList getAttributeTableFixedWidth(int percentage) {
		AttributeList attrs = new AttributeList();
		attrs.add(ATTRIBUTE_BORDER, "1");
		attrs.add("style", "table-layout:fixed;width:" + percentage + "%");
		return attrs;
	}

	/**
	 * Return a th/td-tag align-right HTML attribute.
	 */
	protected ch.softenvironment.jomm.serialize.AttributeList getAttributeAlignRight() {
		return new ch.softenvironment.jomm.serialize.AttributeList(ATTRIBUTE_ALIGN, "right");
	}

	/**
	 * Return a th/td-tag width HTML attribute.
	 *
	 * @see #YEAR_COLUMN_WITH_STYLE
	 */
	protected ch.softenvironment.jomm.serialize.AttributeList getAttributeWidth(Integer width) {
		return new ch.softenvironment.jomm.serialize.AttributeList("style", ATTRIBUTE_WIDTH + ": " + width.toString() + "px;");
	}

	/**
	 * Return the NLS name of given code or assume as "undefined".
	 *
	 * @param code DbCodeType
	 */
	public static String getCodeName(Object code) {
		if (code instanceof DbCodeType) {
			return ((DbCodeType) code).getNameString();
		} else {
			return ResourceManager.getResource(ReportTool.class, "CIUndefinedCode");
		}
	}

	/**
	 * @see #amount(double)
	 */
	protected String getCostUnit() {
		try {
			return getCostUnit(utility.getSystemParameter().getDefaultCurrency());
		} catch (Exception e) {
			log.warn("INGORED: SystemParameters FAILURE: " + e.getLocalizedMessage());
			return UNKNOWN;
		}
	}

	/**
	 * @see #amount(double)
	 */
	protected String getCostUnit(org.tcotool.model.Currency currency) {
		String unit = "";
		try {
			SystemParameter sysParams = utility.getSystemParameter();
			if (DbEnumeration.isIliCode(sysParams.getReportCostExponent(), CostExponent.INTHOUSAND)) {
				unit = getRsc("CIInKilo") + " ";
			} else if (DbEnumeration.isIliCode(sysParams.getReportCostExponent(), CostExponent.INMILLION)) {
				unit = getRsc("CIInMillion") + " ";
			}
			return unit + (currency == null ? UNKNOWN : currency.getNameString());
		} catch (Exception e) {
			log.warn("SystemParameter fault <CostUnit>: " + e.getLocalizedMessage());
			return "";
		}
	}

	/**
	 * Return the complete CSV contents of generated Stream.
	 */
	public String getCsvContents() {
		return getCsvWriter().getWriter().toString();
	}

	/**
	 * Return the complete HTML contents of generated Stream.
	 */
	public String getHTMLContents() {
		return getWriter().toString();
	}

	/**
	 * @return Report-Title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return the TcoObject typical Icon as HTML image-Tag.
	 *
	 * @see ModelUtility#getImageURL(Class)
	 */
	public static String getTcoObjectImage(ModelUtility utility, Class<? extends TcoObject> tcoObject) {
		URL url = utility.getImageURL(tcoObject);
		if (tcoObject.equals(TcoPackage.class) || tcoObject.equals(TcoModel.class)) {
			url = ch.ehi.basics.i18n.ResourceBundle.getURL(ModelUtility.class, StringUtils.getPureClassName(TcoPackage.class) + ".png");
		}
		return "<img src='" + url + "' border='0' alt='" + ModelUtility.getTypeString(tcoObject) + ":'>";
	}

	/**
	 * @param level Chapter level
	 * @param text well formatted text
	 */
	@Override
	public void header(int level, String text) throws IOException {
		// super.header(level, text);
		if (level < 4) {
			nativeContent("<h" + level + ">" + text + "</h" + level + ">");
		} else {
			nativeContent("<h4>" + text + "</h4>");
		}
		getCsvWriter().newline();
		getCsvWriter().cell(level + " - " + text);
		getCsvWriter().newline();
	}

	protected void header(String prefix, int chapterLevel, TcoObject object) throws IOException {
		// super.header(level, text);
		if (chapterLevel == -1) {
			// suppress chapter
			nativeContent("<h4>" + linkObject(object, true) + "</h4>");
		} else if (chapterLevel < 4) {
			nativeContent("<h" + chapterLevel + ">" + prefix + " " + linkObject(object, true) + "</h" + chapterLevel + ">");
		} else {
			nativeContent("<h4>" + prefix + " " + linkObject(object, true) + "</h4>");
		}

		getCsvWriter().newline();
		getCsvWriter().cell(prefix + " " + ModelUtility.getTypeString(object.getClass()) + ": " + object.getName());
		getCsvWriter().newline();
	}

	private String linkObject(TcoObject object, boolean hasRef) {
		return linkObject(object, hasRef, true);
	}

	/**
	 * Create a HTML-link to TcoObject in Configuration.
	 *
	 * @param object to create hyperlink for
	 * @param hasRef true means <a href=object#id..
	 * @param includingType true prints type of object first
	 */
	protected String linkObject(TcoObject object, boolean hasRef, boolean includingType) {
		String text = "";
		if (includingType) {
			text = getTcoObjectImage(utility, object.getClass()) + HtmlSerializer.SPACE; // ModelUtility.getTypeString(object.getClass())
			// +
			// ": ";
		}

		if (hasRef) {
			text = text + "<a href=\"" + object.getId() + "\">";
		}
		text = text + encodeString(object.getName());
		if (hasRef) {
			text = text + "</a>";
		}

		return text;
	}

	/**
	 * Add a HTML-Hyperlink within a table-cell to a concrete TcoObject.
	 *
	 * @param object
	 * @param bold
	 * @throws IOException
	 */
	protected void tableDataLinked(TcoObject object, boolean bold) throws IOException {
		nativeContent("<td>" + (bold ? "<b>" : "") + linkObject(object, true, false) + (bold ? "</b>" : "") + "</td>");

		getCsvWriter().cell(object.getName());
	}

	/**
	 * Add a HTML-Hyperlink within a table-cell to a concrete DbCodeType.
	 *
	 * @param object
	 * @param bold
	 * @throws IOException
	 */
	protected void tableDataLinked(DbCodeType object, boolean bold) throws IOException {
		nativeContent("<td>" + (bold ? "<b>" : "") + "<a href=\"" + ((DbObject) object).getId() + "\">" + encodeString(object.getNameString())
			+ (bold ? "</b>" : "") + "</td>");

		getCsvWriter().cell(object.getNameString());
	}

	/**
	 * Add a HTML-Hyperlink within a table-cell to any given Object.
	 *
	 * @param id
	 * @param text
	 * @param bold
	 * @throws IOException
	 */
	protected void tableDataLinked(final String id, String text, boolean bold) throws IOException {
		nativeContent("<td>" + (bold ? "<b>" : "") + "<a href=\"" + id + "\">" + encodeString(text) + (bold ? "</b>" : "") + "</td>");

		getCsvWriter().cell(text);
	}

	@Override
	public void startTable(AttributeList attrs) throws java.io.IOException {
		super.startTable(attrs);

		getCsvWriter().newline();
	}

	/**
	 * This table only has indentation character -> prints vertical indentation Bars on the left. (Irrelevant for CSV)
	 *
	 * @see #endTreeHeader(int)
	 */
	protected void startTreeHeader(int level) throws IOException {
		if (level > 1) {
			startTable(0);
			startTableRow();
			// print data
			AttributeList attrs = new AttributeList();
			attrs.add(ATTRIBUTE_BORDER, "0");
			attrs.add(ATTRIBUTE_WIDTH, "20");

			// show for each tree-indentation another bar-color at the left
			if (level % 2 == 0.0) {
				attrs.add(ATTRIBUTE_BACKGROUND_COLOR, "#8080FF");
			} else {
				attrs.add(ATTRIBUTE_BACKGROUND_COLOR, "#8080C0");
			}
			tableData("" /* indentation */, attrs);
			startElement("td");
		}
	}

	@Override
	public void tableData(final String text, AttributeList attrs) throws IOException {
		super.tableData(text, attrs);

		getCsvWriter().cell(text);
	}

	protected void tableDataAmount(double value, boolean bold) throws IOException {
		tableDataAmount(value, bold, 1);
	}

	protected void tableDataAmount(double value, boolean bold, int colspan) throws IOException {
		AttributeList attrs = getAttributeAlignRight(); // "<align=\"right\">"
		if (colspan > 1) {
			attrs.add("colspan", "" + colspan);
			getCsvWriter().colSpan(colspan);
		}

		nativeContent("<td");
		writeAttributes(attrs);

		if (bold) {
			nativeContent("><b");
		}
		nativeContent(">" + amount(value));
		if (bold) {
			nativeContent("</b>");
		}
		nativeContent("</td>");

		getCsvWriter().cell(new Double(value));
	}

	/**
	 * Encode a Table-cell with a linked code.
	 *
	 * @param code DbCodeType or String
	 * @throws IOException
	 */
	protected final void tableDataCode(Object code) throws IOException {
		if (code instanceof DbCodeType) {
			tableDataLinked((DbCodeType) code, false);
		} else {
			// <Undefined> Code
			tableDataLinked((String) code, getCodeName(code), false);
		}
	}

	/**
	 *
	 */
	protected void tableDataBold(final String text) throws IOException {
		nativeContent("<td><b>" + encodeString(text) + "</b></td>");

		getCsvWriter().cell(text);
	}

	protected void tableDataHour(Number value, boolean bold) throws IOException {
		if (value == null) {
			tableData("");
		} else {
			tableDataHour(value.doubleValue(), bold);
		}
	}

	protected void tableDataHour(double value, boolean bold) throws IOException {
		AttributeList attrs = getAttributeAlignRight(); // "<align=\"right\">"
		/*
		 * if (colspan > 1) { attrs.add("colspan", "" + colspan); getCsvWriter().colSpan(colspan); }
		 */
		nativeContent("<td");
		writeAttributes(attrs);

		if (bold) {
			nativeContent("><b");
		}
		nativeContent(">" + hourFormat.format(value));
		if (bold) {
			nativeContent("</b>");
		}
		nativeContent("</td>");

		getCsvWriter().cell(new Double(value));
	}

	@Override
	public void tableHeader(final String text, AttributeList attrs) throws IOException {
		super.tableHeader(text, attrs);

		getCsvWriter().cell(text);
	}

	/**
	 * Summarize CostTypes over all services below root.
	 */
	protected void totalCost(TcoObject root) throws java.lang.Exception {
		printDetails = false;

		startHtml();
		startBody(null);
		// calculate the model
		header("1", 1, root);
		paragraph(getRsc("CIAllCost"));

		startParagraph();
		encodeCodes(root.getObjectServer(), root);
		endParagraph();

		endElement(/* body */);
		endElement(/* html */);
	}

	/**
	 * Visitor.
	 *
	 * @param level recursion deepness of packages
	 */
	private void walkPackage(org.tcotool.model.TcoPackage root, String prefix, int level) throws Exception {
		if (printDetails) {
			// given Package
			header(prefix, level, root);
			startTreeHeader(level);
		}

		int subChapter = 1;

		// 1) services in given object
		Iterator<Service> services = root.getService().iterator();
		// report services within Package
		while (services.hasNext()) {
			Service service = services.next();
			// calculates totalCost
			if (printDetails) {
				// print the current Service
				header(prefix + "." + subChapter++, level + 1, service);
				startParagraph();
				// TODO NASTY: usually CostCause code-list not wanted here
				encodeCostBlock(service, service.getObjectServer().retrieveCodes(CostCause.class), Calculator.COST_CAUSE_UNDEFINED,
					ModelUtility.getTypeString(CostCause.class));
				endParagraph();
			}
		}

		// 2) report sub-packages
		Iterator<TcoPackage> groups = root.getOwnedElement().iterator();
		while (groups.hasNext()) {
			walkPackage(groups.next(), prefix + "." + subChapter++, level + 1);
		}

		if (printDetails) {
			endTreeHeader(level);
		}
	}

	@Override
	public void startHtml() throws IOException {
		super.startHtml();
		// TODO verify language in case non supported language and default
		// English resource
		startHead(getTitle(), Locale.getDefault().getLanguage(), null /* "UTF-8" */);
		// other metadata
		endElement(/* head */);
	}

	/**
	 * Convenience Method.
	 *
	 * @param property
	 * @return
	 */
	protected String getRsc(String property) {
		return ResourceManager.getResourceAsNonLabeled(ReportTool.class, property);
	}

	/**
	 * Return a Writer to generate a CSV-Stream.
	 *
	 * @return Serializer for *.csv
	 */
	protected CsvSerializer getCsvWriter() {
		return csvWriter;
	}
}
