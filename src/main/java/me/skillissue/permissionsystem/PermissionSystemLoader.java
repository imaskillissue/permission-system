package me.skillissue.permissionsystem;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.jetbrains.annotations.NotNull;

public class PermissionSystemLoader implements PluginLoader {

  @Override
  public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
    MavenLibraryResolver resolver = new MavenLibraryResolver();
    resolver.addDependency(
        new Dependency(new DefaultArtifact("mysql:mysql-connector-java:8.0.33"), null));
    resolver.addDependency(
        new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.1.0"), null));
    pluginClasspathBuilder.addLibrary(resolver);
  }
}
