package org.pofay.commands;

import picocli.CommandLine.Command;

@Command(name = "top", mixinStandardHelpOptions = true, subcommands = {
        CacheBenchmarkV1.class,
        NoVisibilityExample.class
})
public class CommandHandler implements Runnable {

    @Override
    public void run() {
        System.out.printf("Specify a subcommand: cache-benchmark or no-visibility");
    }

}
