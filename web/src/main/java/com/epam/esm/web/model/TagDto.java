package com.epam.esm.web.model;

import com.epam.esm.web.validator.constraint.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public final class TagDto extends RepresentationModel<TagDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;

    @NotNull(message = "{tag.dto.name.null}")
    @NullOrNotBlank(message = "{tag.dto.name.blank}")
    @Size(max = 50, message = "{tag.dto.name.size}")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+", message = "{tag.dto.name.invalid}")
    private final String name;

    @JsonCreator
    public TagDto(@JsonProperty("id") Long id,
                  @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id) &&
                Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
