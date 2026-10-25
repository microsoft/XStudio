package com.microsoft.hydralab.common.util;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.models.Group;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GraphAPIUtilsTest {

    @Test
    public void testCreateSecurityGroup() {
        // Mock the authentication provider
        IAuthenticationProvider authProvider = Mockito.mock(IAuthenticationProvider.class);

        // Mock the GraphServiceClient
        GraphServiceClient graphClient = Mockito.mock(GraphServiceClient.class);
        Mockito.when(graphClient.groups().buildRequest().post(Mockito.any(Group.class))).thenReturn(null);

        // Call the method to be tested
        Group result = GraphAPIUtils.createSecurityGroup(authProvider);

        // Verify the result
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetMeUser() {
        // Mock the authentication provider
        IAuthenticationProvider authProvider = Mockito.mock(IAuthenticationProvider.class);

        // Mock the GraphServiceClient
        GraphServiceClient graphClient = Mockito.mock(GraphServiceClient.class);
        Mockito.when(graphClient.me().buildRequest().get()).thenReturn(null);

        // Call the method to be tested
        User result = GraphAPIUtils.getMeUser(authProvider);

        // Verify the result
        Assert.assertNotNull(result);
    }
}