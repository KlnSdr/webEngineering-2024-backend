package com.sks.fridge.api;

/**
 * Enum representing the types of requests that can be made to the fridge service.
 */
public enum FridgeRequestType {
    /** Request to get items from the fridge. */
    GET,

    /** Request to update items in the fridge. */
    UPDATE,

    /** Request to delete items from the fridge. */
    DELETE
}