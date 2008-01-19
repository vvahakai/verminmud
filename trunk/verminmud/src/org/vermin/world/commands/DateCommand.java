package org.vermin.world.commands;

import org.vermin.mudlib.Command;
import org.vermin.mudlib.Living;

import org.vermin.world.calendar.*;

public class DateCommand implements Command {
   
   public boolean action(Living who, String params) {
      who.notice(VerminCalendar.getInstance().format("Current date is: %e. %B, year %Y, %T"));
    return false;      
   }
}

