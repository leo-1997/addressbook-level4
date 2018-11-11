package seedu.saveit.commons.events.model;

import seedu.saveit.commons.events.BaseEvent;

/**
 * Indicates model directory was changed.
 */
public class AddTagEvent extends BaseEvent {

    public AddTagEvent() {}

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
