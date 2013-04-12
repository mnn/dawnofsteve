/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class DoSCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "dos";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (astring.length <= 0) {
            icommandsender.sendChatToPlayer("No parameters.");
        }

        String cmd = astring[0];

        //if(cmd.equals("d_"))
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender) {
        return "Dawn of Steve - TODO!";
    }
}
