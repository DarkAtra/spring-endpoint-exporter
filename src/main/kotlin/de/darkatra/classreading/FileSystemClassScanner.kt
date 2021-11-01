package de.darkatra.classreading

import de.darkatra.classreading.type.ClassMetadata
import org.springframework.core.io.FileSystemResourceLoader
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.io.path.pathString

class FileSystemClassScanner(
    includeFilters: List<Pattern> = emptyList(),
    excludeFilters: List<Pattern> = emptyList()
) : ClassScanner(
    includeFilters = includeFilters,
    excludeFilters = excludeFilters,
    resourceLoader = FileSystemResourceLoader()
) {

    /**
     * @param entrypoint the directory to scan
     */
    override fun scan(entrypoint: Path): List<ClassMetadata> {

        // pattern to find all class files in a directory
        // note: Path.resolve with wildcards (aka. *) does not work on windows
        val resourcePattern = entrypoint.normalize().pathString + "/**/*.class"

        // get the resources
        return resourcePatternResolver.getResources(resourcePattern)
            // obtain the class metadata
            .map { resource -> MetadataReader(resource).classMetadata }
            // apply all include and exclude filters
            .filter(this::isCandidate)
    }
}
