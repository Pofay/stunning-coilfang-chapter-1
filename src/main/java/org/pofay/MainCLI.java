package org.pofay;

import org.pofay.commands.CommandHandler;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
public class MainCLI implements QuarkusApplication {

    @Inject
    CommandLine.IFactory factory; 

    @Override
    public int run(String... args) {
        return new CommandLine(new CommandHandler(), factory)
                .execute(args);
    }
}