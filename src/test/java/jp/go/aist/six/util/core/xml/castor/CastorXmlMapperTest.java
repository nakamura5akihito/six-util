package jp.go.aist.six.util.core.xml.castor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import jp.go.aist.six.util.core.config.spring.SpringContext;
import jp.go.aist.six.util.core.xml.spring327.CastorMarshaller;
import jp.go.aist.six.util.xml.XmlMapper;
import model.common.GeneratorType;
import model.definitions.OvalDefinitions;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
//import org.springframework.oxm.castor.CastorMarshaller;


public class CastorXmlMapperTest
{

    private static final String  _CONTEXT_PATH_ =
                    "jp/go/aist/six/util/core/xml/castor/test_context_xml-castor.xml";

    private static final String  _XML_MAPPING_RESOURCE_ = "classpath:jp/go/aist/six/oval/core/xml/castor-xml_oval-5-definitions.xml";
    private static final String  _XML_FILE_ = "src/test/resources/data/oval/mitre-oval-def-5_10/unix/oval-def-p-20743-231_cve-2013-2465.xml";


    /**
     * TEST: if the Castor XML mapping resource is loaded.
     * @throws Exception
     */
    @Test
    public void testLodingClasspathResource()
    throws Exception
    {
        @SuppressWarnings( "resource" )
        ApplicationContext  context = new ClassPathXmlApplicationContext();
        Resource  res = context.getResource( _XML_MAPPING_RESOURCE_ );
        System.out.println( "resource URL: " + res.getURL() );
        assertThat( res, is( notNullValue() ) );
    }



    @Test
    public void testXmlMarshallerBean()
    throws Exception
    {
//        SampleContext  context = new SampleContext();
//        CastorMarshaller  marshaller = context.getBean( "marshaller", CastorMarshaller.class );
//        assertThat( marshaller, is( notNullValue() ) );

        CastorMarshaller  marshaller = new CastorMarshaller();
        assertThat( marshaller.supports( OvalDefinitions.class ), is( true ) );
        assertThat( marshaller.supports( GeneratorType.class ), is( true ) );

        @SuppressWarnings( "resource" )
        ApplicationContext  app_context = new ClassPathXmlApplicationContext( _CONTEXT_PATH_ );
        Resource  res = app_context.getResource( _XML_MAPPING_RESOURCE_ );
        System.out.println( ">Castor XML mapping resource: " + res.getDescription() );
        assertThat( res, is( notNullValue() ) );
        marshaller.setMappingLocations( new Resource[] { res } );

//        marshaller.setProcessExternalEntities( true );    //@since Spring 3.2.8 --> does not affect unmarshalling

        marshaller.setIgnoreExtraAttributes( false );
        marshaller.setIgnoreExtraElements( false );
        marshaller.setSuppressXsiType( true );
        marshaller.setEncoding( "UTF-8" );

        String  schema_location = "http://oval.mitre.org/XMLSchema/oval-common-5 oval-common-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5 oval-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5 oval-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-results-5 oval-results-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-variables-5 oval-variables-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#aix aix-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#esx esx-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#hpux hpux-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#independent independent-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#ios ios-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#linux linux-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#macos macos-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#pixos pixos-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#solaris solaris-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#unix unix-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-definitions-5#windows windows-definitions-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#aix aix-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#esx esx-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#hpux hpux-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#independent independent-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#linux linux-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#macos macos-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#solaris solaris-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#unix unix-system-characteristics-schema.xsd " +
                        "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5#windows windows-system-characteristics-schema.xsd";
        marshaller.setSchemaLocation( schema_location );

        Map<String,String>  ns_mappings = new HashMap<String,String>();
        ns_mappings.put( "xsi", "http://www.w3.org/2001/XMLSchema-instance" );
        ns_mappings.put( "oval", "http://oval.mitre.org/XMLSchema/oval-common-5" );
        ns_mappings.put( "oval-def", "http://oval.mitre.org/XMLSchema/oval-definitions-5" );
        ns_mappings.put( "oval-sc", "http://oval.mitre.org/XMLSchema/oval-system-characteristics-5" );
        ns_mappings.put( "oval-res", "http://oval.mitre.org/XMLSchema/oval-results-5" );
        ns_mappings.put( "opensearch", "http://a9.com/-/spec/opensearch/1.1/" );
        marshaller.setNamespaceMappings( ns_mappings );

        marshaller.afterPropertiesSet();

        CastorXmlMapper  mapper = new CastorXmlMapper();
        mapper.setUnmarshaller( marshaller );
        mapper.setMarshaller( marshaller );

        File  file = new File( _XML_FILE_ );
        assertThat( true, is( file.exists() ) );
        FileInputStream  finput = new FileInputStream( file );

        /* (1) unmarshal */
        System.out.println( ">unmarshal from: " + file.getCanonicalPath() );
        Object  obj = mapper.unmarshal( finput );
        assertThat( obj instanceof OvalDefinitions, is( true ) );
    }



//    @Test
//    public void test()
//    throws Exception
//    {
//        SampleContext  context = new SampleContext();
//
//        XmlMapper  mapper = context.getXmlMapper();
//        File  file = new File( _XML_FILE_ );
//
//        /* (1) unmarshal */
//        System.out.println( ">unmarshal from: " + file.getCanonicalPath() );
//        Object  obj = mapper.unmarshal( new FileInputStream( file ) );
//        assertThat( obj instanceof OvalDefinitions, is( true ) );
//
//    }



    ////////////////////////////////////////////////////////////////////////////
    // sample context
    ////////////////////////////////////////////////////////////////////////////

    public class SampleContext
    extends SpringContext
    {
        public SampleContext()
        {
            this( _CONTEXT_PATH_ );
        }


        public SampleContext(
                        final String config_location
                        )
        {
            super( config_location, new String[] {
                        "classpath:jp/go/aist/six/util/core/xml/castor/test_xml.properties"
                    } );

        }


        public XmlMapper getXmlMapper()
        {
            XmlMapper  mapper = getBean( "xml-mapper", XmlMapper.class );
            return mapper;
        }
    }
    //

}
//
