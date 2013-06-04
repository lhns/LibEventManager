package com.dafttech.eventmanager;

/**
 * Use this Interface to create an onEvent Listener
 */
public interface IEventListener
{
    public Object onEvent(Event event, Object[] objects);
}