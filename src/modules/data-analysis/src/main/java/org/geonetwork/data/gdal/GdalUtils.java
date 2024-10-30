/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.geonetwork.data.model.DataFormat;

public class GdalUtils {

    private static final String GDAL_FORMAT_REGEX = " +(\\w+) +-([\\w,]+)- \\(([rwvso\\+]+)\\):\\ (.*)";
    private static final String GDAL_LAYERS_REGEX = "[0-9]+: ([:\\w]+) +\\(.*\\)";

    protected static List<DataFormat> parseFormats(String output) {
        Pattern pattern = Pattern.compile(GDAL_FORMAT_REGEX);
        return Arrays.stream(output.split(System.lineSeparator()))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> DataFormat.builder()
                        .name(matcher.group(1))
                        .dataType(matcher.group(2))
                        .rwFlag(matcher.group(3))
                        .description(matcher.group(4))
                        .build())
                .toList();
    }

    protected static List<String> parseLayers(String output) {
        Pattern pattern = Pattern.compile(GDAL_LAYERS_REGEX);
        // TODO: may contains title
        //  1: ms:ALEARG_REALISE (title: Exposition au retrait-gonflement des argiles)
        // or geom type
        // 1: CEEUBG100kV2_1 (Line String)
        return Arrays.stream(output.split(System.lineSeparator()))
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1))
                .toList();
    }

    protected static Optional<String> execute(CommandLine command, int timeoutInSeconds) {
        DefaultExecutor executor = DefaultExecutor.builder().get();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExecuteWatchdog watchdog = ExecuteWatchdog.builder()
                .setTimeout(Duration.ofSeconds(timeoutInSeconds))
                .get();
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(new PumpStreamHandler(outputStream));
        try {
            executor.execute(command, resultHandler);
            resultHandler.waitFor();
            return Optional.of(outputStream.toString(UTF_8));
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Optional<String> executeCommand(CommandLine cmdLine, int timeoutInSeconds, String... args) {
        for (int i = 0; i < args.length; i++) {
            cmdLine.addArgument(args[i]);
        }
        return execute(cmdLine, timeoutInSeconds);
    }
}
