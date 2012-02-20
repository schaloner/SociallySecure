package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class LinkedAccountSecurity extends Model
{
    public boolean publiclyVisible = false;
}
