package gov.cms.qpp.conversion.decode;

import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.util.ClasspathHelper;

import gov.cms.qpp.conversion.Context;
import gov.cms.qpp.conversion.model.Node;
import gov.cms.qpp.conversion.model.TemplateId;
import gov.cms.qpp.conversion.xml.XmlException;
import gov.cms.qpp.conversion.xml.XmlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;

class QualitySectionDecoderTest {

	private QualitySectionDecoder sectionDecoder;
	private Node node;

	@BeforeEach
	void setUp() {
		node = new Node();
		sectionDecoder = new QualitySectionDecoder(new Context());
	}


	/**
	 * This tests the QualitySectionDecoder
	 * This decoders exists as a child of Clinical Document Decoder which is not tested here.
	 *
	 * @throws XmlException when parsing invalid xml fragment
	 */
	@Test
	void testInternalDecodeValidXml() throws XmlException {
		String validXML = getValidXML();
		Element element = XmlUtils.stringToDom(validXML);

		sectionDecoder.decode(element, node);
		assertThat(node.getValue("category"))
				.isEqualTo("quality");
	}

	@Test
	void testMeasureSectionV4Decoding() throws XmlException, IOException{
		InputStream stream =
			ClasspathHelper.contextClassLoader().getResourceAsStream("correctMultiToSinglePerfMeasureExample.xml");
		String xmlFragment = IOUtils.toString(stream, StandardCharsets.UTF_8);

		Node root = new QrdaDecoderEngine(new Context()).decode(XmlUtils.stringToDom(xmlFragment));
		Node measureSection = root.findFirstNode(TemplateId.MEASURE_SECTION_V5);

		assertThat(measureSection.getValue(QualitySectionDecoder.CATEGORY_SECTION_V5))
			.isEqualTo(TemplateId.CATEGORY_REPORT_V5.getExtension());
	}

	private String getValidXML() {
		String xmlFragment = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ " <ClinicalDocument  "
				+ " 	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  "
				+ " 	xsi:schemaLocation=\"urn:hl7-org:v3 ../CDA_Schema_Files/infrastructure/cda/CDA_SDTC.xsd\"  "
				+ " 	xmlns=\"urn:hl7-org:v3\"  "
				+ " 	xmlns:voc=\"urn:hl7-org:v3/voc\"> "
				+ " <templateId root=\"2.16.840.1.113883.10.20.27.1.2\" extension=\"2018-05-01\"/> "
				+ " <component> "
				+ " 	<structuredBody><!-- QRDA-III Reporting Parameters Section (CMS EP) --> "
				+ " 		<component> "
				+ " 			<section> "
				+ " 				<entry typeCode=\"DRIV\"> "
				+ " 					<act classCode=\"ACT\" moodCode=\"EVN\"> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.17.3.8\"/> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.27.3.23\" extension=\"2016-11-01\"/> "
				+ " 						<id root=\"95944FB8-241B-11E5-1027-09173F13E4C5\"/> "
				+ " 						<code code=\"252116004\" codeSystem=\"2.16.840.1.113883.6.96\" displayName=\"Observation Parameters\"/> "
				+ " 						<effectiveTime> "
				+ " 							<low value=\"20170101\"/> "
				+ " 							<high value=\"20171231\"/> "
				+ " 						</effectiveTime> "
				+ " 					</act> "
				+ " 				</entry> "
				+ " 			</section> "
				+ " 		</component> "
				+ "         <!-- Advancing Care Information Section--> "
				+ " 		<component> "
				+ " 			<section> "
				+ " 				<!-- Measure Section --> "
				+ " 				<templateId root=\"2.16.840.1.113883.10.20.24.2.2\"/> "
				+ " 				<!-- Advancing Care Information Section templateId --> "
				+ " 				<templateId root=\"2.16.840.1.113883.10.20.27.2.5\" extension=\"2017-06-01\"/> "
				+ " 				<component> "
				+ " 					<observation classCode=\"OBS\" moodCode=\"EVN\"> "
				+ " 						<!-- Performance Rate templateId --> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.27.3.30\" extension=\"2016-09-01\"/> "
				+ " 						<code code=\"72510-1\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Performance Rate\"/> "
				+ " 						<statusCode code=\"completed\"/> "
				+ " 						<value xsi:type=\"REAL\" value=\"0.750000\"/> "
				+ " 					</observation> "
				+ " 				</component> "
				+ " 			</section> "
				+ " 		</component> "
				+ "         <component> "
				+ "           <templateId root=\"2.16.840.1.113883.10.20.27.2.1\" extension=\"2017-06-01\"/> "
				+ " 		  <templateId root=\"2.16.840.1.113883.10.20.27.2.3\" extension=\"2016-11-01\"/> "
				+ " 		  <code code=\"55186-1\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"measure section\"/> "
				+ " 		  <title>Measure Section</title> "
				+ " 		  <entry> "
				+ " 			<organizer classCode=\"CLUSTER\" moodCode=\"EVN\"> "
				+ " 				<templateId root=\"2.16.840.1.113883.10.20.24.3.98\"/> "
				+ " 				<templateId root=\"2.16.840.1.113883.10.20.27.3.1\" extension=\"2016-09-01\"/> "
				+ " 				<templateId root=\"2.16.840.1.113883.10.20.27.3.17\" extension=\"2016-11-01\"/> "
				+ " 				<id root=\"95944FB9-241B-11E5-1027-09173F13E4C5\"/> "
				+ " 				<statusCode code=\"completed\"/> "
				+ " 				<!--Measure Reference and Results--> "
				+ " 				<reference typeCode=\"REFR\"> "
				+ " 					<externalDocument classCode=\"DOC\" moodCode=\"EVN\"> "
				+ " 						<id root=\"2.16.840.1.113883.4.738\" extension=\"40280381-51f0-825b-0152-22b98cff181a\"/> "
				+ " 						<code code=\"57024-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Health Quality Measure Document\"/> "
				+ " 						<text>Controlling High Blood Pressure</text> "
				+ " 					</externalDocument> "
				+ " 				</reference> "
				+ " 				<!--Performance Rate--> "
				+ " 				<component> "
				+ " 					<observation classCode=\"OBS\" moodCode=\"EVN\"> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.27.3.14\" extension=\"2016-09-01\"/> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.27.3.25\" extension=\"2016-11-01\"/> "
				+ " 						<templateId root=\"2.16.840.1.113883.10.20.27.3.30\" extension=\"2016-09-01\"/> "
				+ " 						<code code=\"72510-1\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Performance Rate\"/> "
				+ " 						<statusCode code=\"completed\"/> "
				+ " 						<value xsi:type=\"REAL\" value=\"0.842\"/> "
				+ " 						<reference typeCode=\"REFR\"> "
				+ " 							<externalObservation classCode=\"OBS\" moodCode=\"EVN\"> "
				+ " 								<id root=\"3F385926-FFB0-40C9-B916-37827482C31E\"/> "
				+ " 								<code code=\"NUMER\" codeSystem=\"2.16.840.1.113883.5.4\" codeSystemName=\"ActCode\" displayName=\"Numerator\"/> "
				+ " 							</externalObservation> "
				+ " 						</reference> "
				+ " 					</observation> "
				+ " 				</component> "
				+ "	 		    </organizer> "
				+ "	 		  </entry> "
			    + "         </component> "
				+ " 		</structuredBody> "
				+ " </component> "
				+ " </ClinicalDocument> ";
		return xmlFragment;
	}
}
