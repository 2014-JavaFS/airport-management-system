package com.revature.ams.util.interfaces;

public interface Crudable<O> extends Serviceable<O>{
    boolean update(O updatedObject);
    boolean delete(int id);
}
