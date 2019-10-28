package edu.touro.mco152.bm;

/**
 * An interface to be implemented with Objects that are the main worker in a bench marker application.
 * Brings together a read and write to be called on whatever is being tested
 */
public interface Worker {
    void read(ReadableMemory readableMemory);
    void write(WriteableMemory writeableMemory);
}
