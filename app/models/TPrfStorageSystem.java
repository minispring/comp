package models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Humin on 5/3/14.
 */
@Entity
@Table(name="t_prf_storage_system")
@Access(AccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TPrfStorageSystem extends ArrayPrfModel {

}
