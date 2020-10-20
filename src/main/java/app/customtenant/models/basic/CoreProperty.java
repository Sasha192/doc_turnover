package app.customtenant.models.basic;

import app.customtenant.models.abstr.IdentityBaseEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "core_properties", schema = "bcrew_default")
public class CoreProperty
        extends IdentityBaseEntity
        implements Serializable {

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type_value")
    private CorePropertyType typeValue;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "int_value")
    private Integer intValue;

    @Column(name = "decimal_value")
    private Float floatValue;

    @Column(name = "description")
    private String description;

    public CoreProperty() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CorePropertyType getTypeValue() {
        return this.typeValue;
    }

    public void setTypeValue(final CorePropertyType typeValue) {
        this.typeValue = typeValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(final String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public void setIntValue(final int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return this.floatValue;
    }

    public void setFloatValue(final float floatValue) {
        this.floatValue = floatValue;
    }

    public enum CorePropertyType {
        STRING, NUMBER, DECIMAL
    }
}
