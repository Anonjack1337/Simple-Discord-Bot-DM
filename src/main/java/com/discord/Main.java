package com.discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class Main extends ListenerAdapter {
    private static final String BOT_TOKEN = "YOUR_BOT_TOKEN_HERE"; // Replace with your bot token
    private static final String ATTACHMENT_PATH = "test.txt"; // Add an Attachment if you want

    private static final List<String> TEST_MESSAGES = List.of(
            "Hey there! This is a test DM from the Simple Discord Bot DMing tool.",
            "line2",
            "line3",
            "line4",
            "line5"
    );

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new Main())
                .build();

        jda.awaitReady();
        System.out.println("Bot is online");

        for (Guild guild : jda.getGuilds()) {
            sendTestDMs(guild);
        }
    }

    private static void sendTestDMs(Guild guild) {
        guild.loadMembers().onSuccess(members -> {
            Collections.shuffle(members); // Shuffle the order for randomness
            int delay = 0;

            for (Member member : members) {
                if (!member.getUser().isBot() && !hasStaffRole(member)) {
                    scheduler.schedule(() -> {
                        member.getUser().openPrivateChannel().queue(channel -> {
                            channel.sendMessageEmbeds(randomTestEmbed()).queue(
                                    embedMessage -> {
                                        channel.sendFiles(FileUpload.fromData(new File(ATTACHMENT_PATH))).queue(
                                                fileMessage -> System.out.println("Sent test DM to: " + member.getUser().getName()),
                                                error -> System.err.println("Failed to send file to " + member.getUser().getName())
                                        );
                                    },
                                    error -> System.err.println("Failed to send embed to " + member.getUser().getName())
                            );
                        }, error -> System.err.println("Could not open DM with: " + member.getUser().getName()));
                    }, delay, TimeUnit.SECONDS);

                    delay += 10 + new Random().nextInt(10); // Random 10–19 sec delay per DM
                }
            }
        });
    }

    private static boolean hasStaffRole(Member member) {
        for (Role role : member.getRoles()) {
            String name = role.getName().toLowerCase();
            if (name.contains("mod") || name.contains("admin") || name.contains("staff")) {
                return true;
            }
        }
        return false;
    }

    private static MessageEmbed randomTestEmbed() {
        Random rand = new Random();
        String message = TEST_MESSAGES.get(rand.nextInt(TEST_MESSAGES.size()));

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Test Title")
                .setDescription(message)
                .setColor(Color.CYAN)
                .setFooter("Test Footer")
                .setImage("https://cdn2.steamgriddb.com/grid/1974a767627527a2f88ea3f2818676d7.png");

        return embed.build();
    }
}
