package com.money.reaper.service.acquirer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AcquirerManagerFactory {
	
	@Autowired
    private Environment env;

    private List<String> activeAcquirers;
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        String modes = env.getProperty("acquirer.modes", "");
        if (modes.isEmpty()) {
            activeAcquirers = new ArrayList<>();
            return;
        }

        List<String> acquirerModes = Arrays.asList(modes.split(","));
        activeAcquirers = acquirerModes.stream()
            .filter(mode -> Boolean.parseBoolean(env.getProperty("acquirer." + mode, "false")))
            .collect(Collectors.toList());
    }

    public String getRandomAcquirer() {
        if (activeAcquirers.isEmpty()) {
            return null;
        }
        return activeAcquirers.get(random.nextInt(activeAcquirers.size()));
    }

}
