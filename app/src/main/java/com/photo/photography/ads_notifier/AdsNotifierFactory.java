package com.photo.photography.ads_notifier;

import java.util.Vector;

/**
 * This is a factory class for {@link AdsEventNotifier}. It maintains queue of notifiers required by
 * application. Checks whether already present, otherwise creates new and returns it.
 */
public class AdsNotifierFactory {

    public static final int EVENT_NOTIFIER_AD_STATUS = 4;
    /**
     * This is a vector for maintaining all {@link AdsEventNotifier} required by application.
     */
    private static Vector<AdsEventNotifier> _eventNotifiers = null;
    private static AdsNotifierFactory _notifierFactoryInstance = null;


    /**
     * Private constructor.
     */
    private AdsNotifierFactory() {
        _eventNotifiers = new Vector<AdsEventNotifier>();
    }

    /**
     * This method is used to maintain the NotifierFactory class singleton.
     *
     * @return initialized object of type {@link AdsNotifierFactory}.
     */
    public static AdsNotifierFactory getInstance() {
        if (_notifierFactoryInstance == null) {
            _notifierFactoryInstance = new AdsNotifierFactory();
        }
        return _notifierFactoryInstance;
    }

    /**
     * This function checks whether EventNotifier for specified category is already created or not
     * depending on specified category.
     *
     * @param eventCategory Integer indicating constant for require category.
     * @return {@link AdsEventNotifier} if present, otherwise null.
     */
    private static AdsEventNotifier findNotifier(int eventCategory) {

        AdsEventNotifier eventNotifierObject = null;

        int length = _eventNotifiers.size();

        for (int index = 0; index < length; index++) {

            eventNotifierObject = (AdsEventNotifier) _eventNotifiers.elementAt(index);
            int category = eventNotifierObject.getEventCategory();

            if (eventCategory == category) {

                return eventNotifierObject;
            }
            eventNotifierObject = null;
        }
        return eventNotifierObject;
    }

    /**
     * This function is called to get {@link AdsEventNotifier} for specified event category. New
     * {@link AdsEventNotifier} is created if not present in queue, otherwise returns notifier from
     * queue.
     *
     * @param eventCategory integer constant indicating category of required {@link AdsEventNotifier}.
     * @return {@link AdsEventNotifier} for required category.
     */
    public AdsEventNotifier getNotifier(int eventCategory) {

        /**
         * Check whether present in queue
         */
        AdsEventNotifier eventNotifier = findNotifier(eventCategory);
        /**
         * If present, return EventNotifier from queue.
         */
        if (eventNotifier != null) {

            return eventNotifier;
        }

        /**
         * Required EventNotifier is not present in queue, so create new, add it to the queue and
         * return it
         */
        AdsEventNotifier objEventNotifier = new AdsEventNotifier(eventCategory);
        _eventNotifiers.addElement(objEventNotifier);

        return objEventNotifier;
    }
}