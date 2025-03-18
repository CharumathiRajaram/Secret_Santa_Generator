package com.acme.secret.santa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class that provides a logger instance for all subclasses.
 */
public abstract class BaseLogger {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
