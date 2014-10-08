package org.datacite.schema.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.datacite.schema.SchemaDirectory;
import org.datacite.schema.SchemaUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;

@RunWith(Parameterized.class)
public class ValidationTest {

    private SchemaDirectory schemaDir;
    private File example;

    public ValidationTest(SchemaDirectory schemaDir, File example) {
        this.schemaDir = schemaDir;
        this.example = example;
    }

    @Test
    public void testExampleValid() throws Exception {
        validate(schemaDir);
    }
    
    @Test
    public void testExampleValidToMajorSchema() throws Exception {
        validate(schemaDir.getMajorSchema());
    }
    
    private void validate(SchemaDirectory schemaDir) throws Exception {
        Schema schema = schemaDir.getSchema();
        Validator validator = schema.newValidator();
        Source source = new StreamSource(example);
        validator.validate(source);
    }
    
    @Test
    public void testExampleSchemaLocation() throws Exception {
        Document doc = SchemaUtils.getDocument(example);
        String schemaLocation = SchemaUtils.getSchemaLocation(doc);
        String expectedSchemaLocation = schemaDir.getExpectedSchemaLocation();
        assertEquals(expectedSchemaLocation, schemaLocation);
    }

    @Test
    public void testExampleNamespace() throws Exception {
        Document doc = SchemaUtils.getDocument(example);
        String namespace = SchemaUtils.getNamespace(doc);
        String expectedNamespace = schemaDir.getExpectedSchemaNamespace();
        assertEquals(expectedNamespace, namespace);
    }

    @Parameters(name="{1}")
    public static Collection<Object[]> data() {
        Collection<Object[]> data = new ArrayList<Object[]>();
        List<SchemaDirectory> schemaDirs = SchemaDirectory.getAllSchemaDirectories();
        for (SchemaDirectory schemaDir : schemaDirs)
            for (File example : schemaDir.getExamples()) 
                data.add(new Object[] { schemaDir, example });
        return data;
    }
    
}
