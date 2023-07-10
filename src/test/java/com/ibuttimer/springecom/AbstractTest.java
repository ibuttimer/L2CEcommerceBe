package com.ibuttimer.springecom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibuttimer.springecom.misc.ITestResource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class AbstractTest implements ITestResource {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @LocalServerPort
    private Integer port;

    @Value("${spring.data.rest.base-path}")
    private String apiBasePath;

    protected static ObjectMapper objectMapper;

    protected ResourceBundle bundle;

    public AbstractTest() {
        bundle = getResourceBundle("test");
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return bundle;
    }

    /**
     * Load a resource file
     * @param filename - resource file name
     * @return file contents as byte array
     */
    protected byte[] loadFileBytes(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] fileBytes = new byte[0];

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            fileBytes = Objects.requireNonNull(inputStream).readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * Load a resource file
     * @param inputStream - file input stream
     * @return file contents as byte array
     */
    protected byte[] loadFileBytes(InputStream inputStream) {
        byte[] fileBytes = new byte[0];

        try {
            if (inputStream != null) {
                fileBytes = inputStream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * Load a resource file
     * @param filename - resource file name
     * @return file contents as list of strings
     */
    protected List<String> loadFileLines(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        List<String> lines = Lists.newArrayList();

        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            lines = loadFileLines(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Load a resource file
     * @param inputStream - file input stream
     * @return file contents as list of strings
     */
    protected List<String> loadFileLines(InputStream inputStream) {
        List<String> lines = Lists.newArrayList();

        try (InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            lines = reader.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    @BeforeAll
    public static void beforeAll() {
        // no-op
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterAll
    public static void afterAll() {
        // no-op
    }

    protected String getUrl(String path,
                            String query,
                            String fragment) {
        URI uri = null;
        try {
            uri = new URI("http", null, "localhost", port, path, query, fragment);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail();
        }
        return uri.toString();
    }

    protected String getUrl(String path) {
        return getUrl(path, null, null);
    }

    /**
     * Pause test for specified time
     * @param timeout – the length of time to sleep in milliseconds
     * @throws InterruptedException – see {@link Thread()#pause(int)}
     */
    protected void pause(int timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }

    /**
     * Pause test for specified time
     * @param timeout – resource key for the length of time to sleep in milliseconds
     * @throws InterruptedException – see {@link Thread()#pause(int)}
     */
    protected void pause(String timeout) throws InterruptedException {
        pause(timeout, 1);
    }

    /**
     * Pause test for specified time
     * @param timeout – resource key for the length of time to sleep in milliseconds
     * @param multiplier - number of multiples of timeout to wait
     * @throws InterruptedException – see {@link Thread()#pause(int)}
     */
    protected void pause(String timeout, int multiplier) throws InterruptedException {
        pause(getResourceInt(timeout) * multiplier);
    }

    /**
     * Pause test
     * @throws InterruptedException – see {@link Thread()#pause(int)}
     */
    protected void pause() throws InterruptedException {
        pause("defaultEoTTimeout");
    }

    protected static void clearRepository(CrudRepository<?, Long> repository) {
        repository.deleteAll();
        assertFalse(repository.findAll().iterator().hasNext(), () -> "Repository not empty: " + repository.getClass().getSimpleName());
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }
}
