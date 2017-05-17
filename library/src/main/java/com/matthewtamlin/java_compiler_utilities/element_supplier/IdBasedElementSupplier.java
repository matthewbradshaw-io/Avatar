package com.matthewtamlin.java_compiler_utilities.element_supplier;

import com.matthewtamlin.java_compiler_utilities.collectors.ElementCollector;
import com.matthewtamlin.java_compiler_utilities.collectors.IdBasedElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class IdBasedElementSupplier {
	private JavaFileObject source;
	
	public IdBasedElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	public Set<Element> getElementsWithId(final String id) throws CompilerMissingException {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		final ElementCollector<Set<Element>> collector = new IdBasedElementCollector(id);
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
	
	public Element getUniqueElementWithId(final String id) throws CompilerMissingException {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		final Set<Element> elements = getElementsWithId(id);
		
		if (elements.isEmpty()) {
			throw new UniqueElementNotFoundException("No elements found for ID: " + id);
		}
		
		if (elements.size() > 1) {
			throw new UniqueElementNotFoundException("Multiple elements found for ID: " + id);
		}
		
		return elements.iterator().next();
	}
}