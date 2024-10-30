/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.editorconfig;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the org.fao.geonet.kernel.schema.editorconfig package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java
 * representation for XML content. The Java representation of XML content can consist of schema
 * derived interfaces and classes representing the binding of schema type definitions, element
 * declarations and model groups. Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _Name_QNAME = new QName("", "name");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: org.fao.geonet.kernel.schema.editorconfig
     */
    public ObjectFactory() {}

    /** Create an instance of {@link Key } */
    public Key createKey() {
        return new Key();
    }

    /** Create an instance of {@link ThesaurusList } */
    public ThesaurusList createThesaurusList() {
        return new ThesaurusList();
    }

    /** Create an instance of {@link BatchEditing } */
    public BatchEditing createBatchEditing() {
        return new BatchEditing();
    }

    /** Create an instance of {@link BatchEditing.Section } */
    public BatchEditing.Section createBatchEditingSection() {
        return new BatchEditing.Section();
    }

    /** Create an instance of {@link Template } */
    public Template createTemplate() {
        return new Template();
    }

    /** Create an instance of {@link Values } */
    public Values createValues() {
        return new Values();
    }

    /** Create an instance of {@link Key.Codelist } */
    public Key.Codelist createKeyCodelist() {
        return new Key.Codelist();
    }

    /** Create an instance of {@link Key.Helper } */
    public Key.Helper createKeyHelper() {
        return new Key.Helper();
    }

    /** Create an instance of {@link DirectiveAttributes } */
    public DirectiveAttributes createDirectiveAttributes() {
        return new DirectiveAttributes();
    }

    /** Create an instance of {@link Snippet } */
    public Snippet createSnippet() {
        return new Snippet();
    }

    /** Create an instance of {@link Editor } */
    public Editor createEditor() {
        return new Editor();
    }

    /** Create an instance of {@link Fields } */
    public Fields createFields() {
        return new Fields();
    }

    /** Create an instance of {@link For } */
    public For createFor() {
        return new For();
    }

    /** Create an instance of {@link FieldsWithFieldset } */
    public FieldsWithFieldset createFieldsWithFieldset() {
        return new FieldsWithFieldset();
    }

    /** Create an instance of {@link MultilingualFields } */
    public MultilingualFields createMultilingualFields() {
        return new MultilingualFields();
    }

    /** Create an instance of {@link Expanded } */
    public Expanded createExpanded() {
        return new Expanded();
    }

    /** Create an instance of {@link Exclude } */
    public Exclude createExclude() {
        return new Exclude();
    }

    /** Create an instance of {@link Views } */
    public Views createViews() {
        return new Views();
    }

    /** Create an instance of {@link View } */
    public View createView() {
        return new View();
    }

    /** Create an instance of {@link Tab } */
    public Tab createTab() {
        return new Tab();
    }

    /** Create an instance of {@link Section } */
    public Section createSection() {
        return new Section();
    }

    /** Create an instance of {@link Field } */
    public Field createField() {
        return new Field();
    }

    /** Create an instance of {@link Action } */
    public Action createAction() {
        return new Action();
    }

    /** Create an instance of {@link Text } */
    public Text createText() {
        return new Text();
    }

    /** Create an instance of {@link Fieldset } */
    public Fieldset createFieldset() {
        return new Fieldset();
    }

    /** Create an instance of {@link FlatModeExceptions } */
    public FlatModeExceptions createFlatModeExceptions() {
        return new FlatModeExceptions();
    }

    /** Create an instance of {@link ThesaurusList.Thesaurus } */
    public ThesaurusList.Thesaurus createThesaurusListThesaurus() {
        return new ThesaurusList.Thesaurus();
    }

    /** Create an instance of {@link BatchEditing.Section.Field } */
    public BatchEditing.Section.Field createBatchEditingSectionField() {
        return new BatchEditing.Section.Field();
    }

    /** Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}} */
    @XmlElementDecl(namespace = "", name = "name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }
}
