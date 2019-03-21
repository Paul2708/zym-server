package de.paul2708.claim.model.identifier;

import java.util.function.Consumer;

/**
 * This consumer sets the id at accepting.
 *
 * @author Paul2708
 */
public class IdentifierConsumer implements Consumer<Integer> {

    private Identifier identifier;

    /**
     * Create a new identifier consumer.
     *
     * @param identifier identifier
     */
    public IdentifierConsumer(Identifier identifier) {
        this.identifier = identifier;
    }

    /**
     * Performs this operation on the given argument.
     *
     * @param integer the input argument
     */
    @Override
    public void accept(Integer integer) {
        identifier.setId(integer);
    }
}
