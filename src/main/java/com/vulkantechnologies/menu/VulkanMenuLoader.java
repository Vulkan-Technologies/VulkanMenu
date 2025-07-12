package com.vulkantechnologies.menu;

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;

public class VulkanMenuLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        // Repositories
        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://maven-central.storage-download.googleapis.com/maven2").build());

        // Dependencies
        resolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-yaml:4.2.0"), null));

        // Register the resolver with the plugin classpath builder
        pluginClasspathBuilder.addLibrary(resolver);
    }

}
