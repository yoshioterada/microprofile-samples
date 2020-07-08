/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

 package com.microsoft.azure.samples.kumuluzee;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.microsoft.azure.samples.kumuluzee.rest.WorldServiceEndpoint;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/v1")
public class KumuluzEEhelloazureRestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // resources
        classes.add(HelloController.class);
        classes.add(WorldServiceEndpoint.class);

        return classes;
    }
}
