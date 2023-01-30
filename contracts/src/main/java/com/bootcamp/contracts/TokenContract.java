package com.bootcamp.contracts;

import com.bootcamp.states.TokenState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;

import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

public class TokenContract implements Contract {
    public static String ID = "com.bootcamp.contracts.TokenContract";


    public void verify(LedgerTransaction tx) throws IllegalArgumentException {

        if(tx.getInputStates().size() != 0) {
            throw new IllegalArgumentException("Requires 0 inputs in the tx.");
        }

        if(tx.getOutputStates().size() != 1) {
            throw new IllegalArgumentException("Requires 1 output state");
        }

        if(tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("Token Contract requires 1 command");
        }

        if(!(tx.getOutput(0) instanceof TokenState)) {
            throw new IllegalArgumentException("Token Contract requires TokenState as type of the output");
        }

        TokenState tokenState = (TokenState) tx.getOutput(0);

        if(tokenState.getAmount() <= 0) {
            throw new IllegalArgumentException("Token Output amount needs to be gt zero");
        }

        if(!(tx.getCommand(0).getValue() instanceof Commands)) {
            throw new IllegalArgumentException("The command must be an Issue command ");
        }

        if(!(tx.getCommand(0).getSigners().contains(tokenState.getIssuer().getOwningKey()))) {
            throw new IllegalArgumentException("The issuer must be in the signers");
        }

    }


    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }
}