package de.codecentric.elasticsearch.challenge.node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.junit.rules.ExternalResource;

public class ElasticsearchTestNode extends ExternalResource {

  private static final boolean DISABLE_HTTP = false;

  private Node node;
  private Path dataDir;

  @Override
  protected void before() throws Throwable {
    System.out.println("Setting up Elasticsearch test node");
    createDataDir();
    createNode();
  }

  private void createDataDir() {
    try {
      dataDir = Files.createTempDirectory("es-challenge-");
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private void createNode() {
    ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder().put("path.data", dataDir.toString());
    if (DISABLE_HTTP) {
      settings.put("http.enabled", "false");
    }
    node = NodeBuilder.nodeBuilder().local(true).settings(settings.build()).node();
  }

  @Override
  protected void after() {
    System.out.println("Tearing down Elasticsearch test node");
    teardownNode();
    deleteDataDir();
  }

  private void teardownNode() {
    node.close();
  }

  private void deleteDataDir() {
    try {
      FileUtils.deleteDirectory(dataDir.toFile());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public Client getClient() {
    return node.client();
  }
}
