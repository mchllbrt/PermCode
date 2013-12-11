package permlib.processor;

import java.util.Arrays;
import java.util.Collection;
import permlib.Permutation;

/**
 * This class converts a collection of processors into a single processor.
 *
 * @author Michael Albert
 */
public class PermProcessorCollection implements PermProcessor {

    private Collection<PermProcessor> processors;

    /**
     * Creates a single processor from a collection of processors.
     *
     * @param processors the processors
     */
    public PermProcessorCollection(Collection<PermProcessor> processors) {
        this.processors = processors;
    }

    /**
     * Creates a single processor from a list of processor arguments
     *
     * @param processors the processors
     */
    public PermProcessorCollection(PermProcessor... processors) {
        this.processors = Arrays.asList(processors);
    }

    /**
     * Processes a permutation using each of the processors.
     *
     * @param p the permutation
     */
    @Override
    public void process(Permutation p) {
        for (PermProcessor processor : processors) {
            processor.process(p);
        }
    }

    /**
     * Resets each of the processors.
     */
    @Override
    public void reset() {
        for (PermProcessor processor : processors) {
            processor.reset();
        }
    }

    /**
     * Concatenates the reports of each of the processors separated by newlines.
     *
     * @return the concatenated report
     */
    @Override
    public String report() {
        StringBuilder result = new StringBuilder();
        for (PermProcessor processor : processors) {
            result.append(processor.report());
            result.append('\n');
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

}
