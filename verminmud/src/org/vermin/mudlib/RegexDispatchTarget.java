package org.vermin.mudlib;

public interface RegexDispatchTarget {
    
    /**
     * Returns the dispatch configuration for this target.
     * The dispatch configuration is an array of strings, each of which
     * contain one command pattern and the associated method to call.
     * The patterns are tried sequentially until one matches.
     * 
     * A single dispatch configuration has the following form:
     *    &lt;command regex&gt; => &lt;methodname&gt;(&lt;argument&gt;*)
     * Where
     * <ul>
     *    <li><b>command regex</b> is a regular expression pattern
     *    that is matched against the user input.</li>
     *    
     *    <li><b>methodname</b> is the name of the method in this class
     *    to call.</li>
     *    
     *    <li><b>argument</b> is a type and a matching group from the
     *    command regex. The actual value for the argument is determined
     *    by the type.</li>
     *    
     * </ul>
     * 
     * For a list of possible argument types, see resolveValue in RegexDispatch.
     *    
     * @return an array containing the dispatch configuration
     */
	public abstract String[] getDispatchConfiguration();

}
