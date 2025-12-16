package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.app.domain.ChatRoomTestSamples.*;
import static com.monsterdam.app.domain.CommentMentionTestSamples.*;
import static com.monsterdam.app.domain.CountryTestSamples.*;
import static com.monsterdam.app.domain.DirectMessageTestSamples.*;
import static com.monsterdam.app.domain.LikeMarkTestSamples.*;
import static com.monsterdam.app.domain.NotificationTestSamples.*;
import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.PollVoteTestSamples.*;
import static com.monsterdam.app.domain.PostCommentTestSamples.*;
import static com.monsterdam.app.domain.PostFeedTestSamples.*;
import static com.monsterdam.app.domain.PostMentionTestSamples.*;
import static com.monsterdam.app.domain.PurchasedSubscriptionTestSamples.*;
import static com.monsterdam.app.domain.SingleAudioTestSamples.*;
import static com.monsterdam.app.domain.SinglePhotoTestSamples.*;
import static com.monsterdam.app.domain.SingleVideoTestSamples.*;
import static com.monsterdam.app.domain.UserAssociationTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static com.monsterdam.app.domain.UserProfileTestSamples.*;
import static com.monsterdam.app.domain.UserSettingsTestSamples.*;
import static com.monsterdam.app.domain.VideoStoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserLiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserLite.class);
        UserLite userLite1 = getUserLiteSample1();
        UserLite userLite2 = new UserLite();
        assertThat(userLite1).isNotEqualTo(userLite2);

        userLite2.setId(userLite1.getId());
        assertThat(userLite1).isEqualTo(userLite2);

        userLite2 = getUserLiteSample2();
        assertThat(userLite1).isNotEqualTo(userLite2);
    }

    @Test
    void profileTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userLite.setProfile(userProfileBack);
        assertThat(userLite.getProfile()).isEqualTo(userProfileBack);

        userLite.profile(null);
        assertThat(userLite.getProfile()).isNull();
    }

    @Test
    void settingsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserSettings userSettingsBack = getUserSettingsRandomSampleGenerator();

        userLite.setSettings(userSettingsBack);
        assertThat(userLite.getSettings()).isEqualTo(userSettingsBack);

        userLite.settings(null);
        assertThat(userLite.getSettings()).isNull();
    }

    @Test
    void countryOfBirthTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        userLite.setCountryOfBirth(countryBack);
        assertThat(userLite.getCountryOfBirth()).isEqualTo(countryBack);

        userLite.countryOfBirth(null);
        assertThat(userLite.getCountryOfBirth()).isNull();
    }

    @Test
    void purchasedSubscriptionsAsViewerTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        userLite.addPurchasedSubscriptionsAsViewer(purchasedSubscriptionBack);
        assertThat(userLite.getPurchasedSubscriptionsAsViewers()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isEqualTo(userLite);

        userLite.removePurchasedSubscriptionsAsViewer(purchasedSubscriptionBack);
        assertThat(userLite.getPurchasedSubscriptionsAsViewers()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isNull();

        userLite.purchasedSubscriptionsAsViewers(new HashSet<>(Set.of(purchasedSubscriptionBack)));
        assertThat(userLite.getPurchasedSubscriptionsAsViewers()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isEqualTo(userLite);

        userLite.setPurchasedSubscriptionsAsViewers(new HashSet<>());
        assertThat(userLite.getPurchasedSubscriptionsAsViewers()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getViewer()).isNull();
    }

    @Test
    void purchasedSubscriptionsAsCreatorTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PurchasedSubscription purchasedSubscriptionBack = getPurchasedSubscriptionRandomSampleGenerator();

        userLite.addPurchasedSubscriptionsAsCreator(purchasedSubscriptionBack);
        assertThat(userLite.getPurchasedSubscriptionsAsCreators()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getCreator()).isEqualTo(userLite);

        userLite.removePurchasedSubscriptionsAsCreator(purchasedSubscriptionBack);
        assertThat(userLite.getPurchasedSubscriptionsAsCreators()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getCreator()).isNull();

        userLite.purchasedSubscriptionsAsCreators(new HashSet<>(Set.of(purchasedSubscriptionBack)));
        assertThat(userLite.getPurchasedSubscriptionsAsCreators()).containsOnly(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getCreator()).isEqualTo(userLite);

        userLite.setPurchasedSubscriptionsAsCreators(new HashSet<>());
        assertThat(userLite.getPurchasedSubscriptionsAsCreators()).doesNotContain(purchasedSubscriptionBack);
        assertThat(purchasedSubscriptionBack.getCreator()).isNull();
    }

    @Test
    void pollVotesTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PollVote pollVoteBack = getPollVoteRandomSampleGenerator();

        userLite.addPollVotes(pollVoteBack);
        assertThat(userLite.getPollVotes()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getVoter()).isEqualTo(userLite);

        userLite.removePollVotes(pollVoteBack);
        assertThat(userLite.getPollVotes()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getVoter()).isNull();

        userLite.pollVotes(new HashSet<>(Set.of(pollVoteBack)));
        assertThat(userLite.getPollVotes()).containsOnly(pollVoteBack);
        assertThat(pollVoteBack.getVoter()).isEqualTo(userLite);

        userLite.setPollVotes(new HashSet<>());
        assertThat(userLite.getPollVotes()).doesNotContain(pollVoteBack);
        assertThat(pollVoteBack.getVoter()).isNull();
    }

    @Test
    void associationsAsOwnerTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        UserAssociation userAssociationBack = getUserAssociationRandomSampleGenerator();

        userLite.addAssociationsAsOwner(userAssociationBack);
        assertThat(userLite.getAssociationsAsOwners()).containsOnly(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isEqualTo(userLite);

        userLite.removeAssociationsAsOwner(userAssociationBack);
        assertThat(userLite.getAssociationsAsOwners()).doesNotContain(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isNull();

        userLite.associationsAsOwners(new HashSet<>(Set.of(userAssociationBack)));
        assertThat(userLite.getAssociationsAsOwners()).containsOnly(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isEqualTo(userLite);

        userLite.setAssociationsAsOwners(new HashSet<>());
        assertThat(userLite.getAssociationsAsOwners()).doesNotContain(userAssociationBack);
        assertThat(userAssociationBack.getOwner()).isNull();
    }

    @Test
    void assistanceTicketsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        AssistanceTicket assistanceTicketBack = getAssistanceTicketRandomSampleGenerator();

        userLite.addAssistanceTickets(assistanceTicketBack);
        assertThat(userLite.getAssistanceTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isEqualTo(userLite);

        userLite.removeAssistanceTickets(assistanceTicketBack);
        assertThat(userLite.getAssistanceTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isNull();

        userLite.assistanceTickets(new HashSet<>(Set.of(assistanceTicketBack)));
        assertThat(userLite.getAssistanceTickets()).containsOnly(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isEqualTo(userLite);

        userLite.setAssistanceTickets(new HashSet<>());
        assertThat(userLite.getAssistanceTickets()).doesNotContain(assistanceTicketBack);
        assertThat(assistanceTicketBack.getUser()).isNull();
    }

    @Test
    void paymentsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        userLite.addPayments(paymentBack);
        assertThat(userLite.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getViewer()).isEqualTo(userLite);

        userLite.removePayments(paymentBack);
        assertThat(userLite.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getViewer()).isNull();

        userLite.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(userLite.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getViewer()).isEqualTo(userLite);

        userLite.setPayments(new HashSet<>());
        assertThat(userLite.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getViewer()).isNull();
    }

    @Test
    void commentsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PostComment postCommentBack = getPostCommentRandomSampleGenerator();

        userLite.addComments(postCommentBack);
        assertThat(userLite.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isEqualTo(userLite);

        userLite.removeComments(postCommentBack);
        assertThat(userLite.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isNull();

        userLite.comments(new HashSet<>(Set.of(postCommentBack)));
        assertThat(userLite.getComments()).containsOnly(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isEqualTo(userLite);

        userLite.setComments(new HashSet<>());
        assertThat(userLite.getComments()).doesNotContain(postCommentBack);
        assertThat(postCommentBack.getCommenter()).isNull();
    }

    @Test
    void postsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PostFeed postFeedBack = getPostFeedRandomSampleGenerator();

        userLite.addPosts(postFeedBack);
        assertThat(userLite.getPosts()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getCreator()).isEqualTo(userLite);

        userLite.removePosts(postFeedBack);
        assertThat(userLite.getPosts()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getCreator()).isNull();

        userLite.posts(new HashSet<>(Set.of(postFeedBack)));
        assertThat(userLite.getPosts()).containsOnly(postFeedBack);
        assertThat(postFeedBack.getCreator()).isEqualTo(userLite);

        userLite.setPosts(new HashSet<>());
        assertThat(userLite.getPosts()).doesNotContain(postFeedBack);
        assertThat(postFeedBack.getCreator()).isNull();
    }

    @Test
    void singleAudiosTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        SingleAudio singleAudioBack = getSingleAudioRandomSampleGenerator();

        userLite.addSingleAudios(singleAudioBack);
        assertThat(userLite.getSingleAudios()).containsOnly(singleAudioBack);
        assertThat(singleAudioBack.getCreator()).isEqualTo(userLite);

        userLite.removeSingleAudios(singleAudioBack);
        assertThat(userLite.getSingleAudios()).doesNotContain(singleAudioBack);
        assertThat(singleAudioBack.getCreator()).isNull();

        userLite.singleAudios(new HashSet<>(Set.of(singleAudioBack)));
        assertThat(userLite.getSingleAudios()).containsOnly(singleAudioBack);
        assertThat(singleAudioBack.getCreator()).isEqualTo(userLite);

        userLite.setSingleAudios(new HashSet<>());
        assertThat(userLite.getSingleAudios()).doesNotContain(singleAudioBack);
        assertThat(singleAudioBack.getCreator()).isNull();
    }

    @Test
    void singleVideosTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        SingleVideo singleVideoBack = getSingleVideoRandomSampleGenerator();

        userLite.addSingleVideos(singleVideoBack);
        assertThat(userLite.getSingleVideos()).containsOnly(singleVideoBack);
        assertThat(singleVideoBack.getCreator()).isEqualTo(userLite);

        userLite.removeSingleVideos(singleVideoBack);
        assertThat(userLite.getSingleVideos()).doesNotContain(singleVideoBack);
        assertThat(singleVideoBack.getCreator()).isNull();

        userLite.singleVideos(new HashSet<>(Set.of(singleVideoBack)));
        assertThat(userLite.getSingleVideos()).containsOnly(singleVideoBack);
        assertThat(singleVideoBack.getCreator()).isEqualTo(userLite);

        userLite.setSingleVideos(new HashSet<>());
        assertThat(userLite.getSingleVideos()).doesNotContain(singleVideoBack);
        assertThat(singleVideoBack.getCreator()).isNull();
    }

    @Test
    void singlePhotosTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        SinglePhoto singlePhotoBack = getSinglePhotoRandomSampleGenerator();

        userLite.addSinglePhotos(singlePhotoBack);
        assertThat(userLite.getSinglePhotos()).containsOnly(singlePhotoBack);
        assertThat(singlePhotoBack.getCreator()).isEqualTo(userLite);

        userLite.removeSinglePhotos(singlePhotoBack);
        assertThat(userLite.getSinglePhotos()).doesNotContain(singlePhotoBack);
        assertThat(singlePhotoBack.getCreator()).isNull();

        userLite.singlePhotos(new HashSet<>(Set.of(singlePhotoBack)));
        assertThat(userLite.getSinglePhotos()).containsOnly(singlePhotoBack);
        assertThat(singlePhotoBack.getCreator()).isEqualTo(userLite);

        userLite.setSinglePhotos(new HashSet<>());
        assertThat(userLite.getSinglePhotos()).doesNotContain(singlePhotoBack);
        assertThat(singlePhotoBack.getCreator()).isNull();
    }

    @Test
    void videoStoriesTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        VideoStory videoStoryBack = getVideoStoryRandomSampleGenerator();

        userLite.addVideoStories(videoStoryBack);
        assertThat(userLite.getVideoStories()).containsOnly(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isEqualTo(userLite);

        userLite.removeVideoStories(videoStoryBack);
        assertThat(userLite.getVideoStories()).doesNotContain(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isNull();

        userLite.videoStories(new HashSet<>(Set.of(videoStoryBack)));
        assertThat(userLite.getVideoStories()).containsOnly(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isEqualTo(userLite);

        userLite.setVideoStories(new HashSet<>());
        assertThat(userLite.getVideoStories()).doesNotContain(videoStoryBack);
        assertThat(videoStoryBack.getCreator()).isNull();
    }

    @Test
    void directMessagesSentTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        DirectMessage directMessageBack = getDirectMessageRandomSampleGenerator();

        userLite.addDirectMessagesSent(directMessageBack);
        assertThat(userLite.getDirectMessagesSents()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getSender()).isEqualTo(userLite);

        userLite.removeDirectMessagesSent(directMessageBack);
        assertThat(userLite.getDirectMessagesSents()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getSender()).isNull();

        userLite.directMessagesSents(new HashSet<>(Set.of(directMessageBack)));
        assertThat(userLite.getDirectMessagesSents()).containsOnly(directMessageBack);
        assertThat(directMessageBack.getSender()).isEqualTo(userLite);

        userLite.setDirectMessagesSents(new HashSet<>());
        assertThat(userLite.getDirectMessagesSents()).doesNotContain(directMessageBack);
        assertThat(directMessageBack.getSender()).isNull();
    }

    @Test
    void likeMarksTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        LikeMark likeMarkBack = getLikeMarkRandomSampleGenerator();

        userLite.addLikeMarks(likeMarkBack);
        assertThat(userLite.getLikeMarks()).containsOnly(likeMarkBack);
        assertThat(likeMarkBack.getLiker()).isEqualTo(userLite);

        userLite.removeLikeMarks(likeMarkBack);
        assertThat(userLite.getLikeMarks()).doesNotContain(likeMarkBack);
        assertThat(likeMarkBack.getLiker()).isNull();

        userLite.likeMarks(new HashSet<>(Set.of(likeMarkBack)));
        assertThat(userLite.getLikeMarks()).containsOnly(likeMarkBack);
        assertThat(likeMarkBack.getLiker()).isEqualTo(userLite);

        userLite.setLikeMarks(new HashSet<>());
        assertThat(userLite.getLikeMarks()).doesNotContain(likeMarkBack);
        assertThat(likeMarkBack.getLiker()).isNull();
    }

    @Test
    void notificationsReceivedTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userLite.addNotificationsReceived(notificationBack);
        assertThat(userLite.getNotificationsReceiveds()).containsOnly(notificationBack);
        assertThat(notificationBack.getRecipient()).isEqualTo(userLite);

        userLite.removeNotificationsReceived(notificationBack);
        assertThat(userLite.getNotificationsReceiveds()).doesNotContain(notificationBack);
        assertThat(notificationBack.getRecipient()).isNull();

        userLite.notificationsReceiveds(new HashSet<>(Set.of(notificationBack)));
        assertThat(userLite.getNotificationsReceiveds()).containsOnly(notificationBack);
        assertThat(notificationBack.getRecipient()).isEqualTo(userLite);

        userLite.setNotificationsReceiveds(new HashSet<>());
        assertThat(userLite.getNotificationsReceiveds()).doesNotContain(notificationBack);
        assertThat(notificationBack.getRecipient()).isNull();
    }

    @Test
    void notificationsTriggeredTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        userLite.addNotificationsTriggered(notificationBack);
        assertThat(userLite.getNotificationsTriggereds()).containsOnly(notificationBack);
        assertThat(notificationBack.getActor()).isEqualTo(userLite);

        userLite.removeNotificationsTriggered(notificationBack);
        assertThat(userLite.getNotificationsTriggereds()).doesNotContain(notificationBack);
        assertThat(notificationBack.getActor()).isNull();

        userLite.notificationsTriggereds(new HashSet<>(Set.of(notificationBack)));
        assertThat(userLite.getNotificationsTriggereds()).containsOnly(notificationBack);
        assertThat(notificationBack.getActor()).isEqualTo(userLite);

        userLite.setNotificationsTriggereds(new HashSet<>());
        assertThat(userLite.getNotificationsTriggereds()).doesNotContain(notificationBack);
        assertThat(notificationBack.getActor()).isNull();
    }

    @Test
    void postMentionsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        PostMention postMentionBack = getPostMentionRandomSampleGenerator();

        userLite.addPostMentions(postMentionBack);
        assertThat(userLite.getPostMentions()).containsOnly(postMentionBack);
        assertThat(postMentionBack.getMentionedUser()).isEqualTo(userLite);

        userLite.removePostMentions(postMentionBack);
        assertThat(userLite.getPostMentions()).doesNotContain(postMentionBack);
        assertThat(postMentionBack.getMentionedUser()).isNull();

        userLite.postMentions(new HashSet<>(Set.of(postMentionBack)));
        assertThat(userLite.getPostMentions()).containsOnly(postMentionBack);
        assertThat(postMentionBack.getMentionedUser()).isEqualTo(userLite);

        userLite.setPostMentions(new HashSet<>());
        assertThat(userLite.getPostMentions()).doesNotContain(postMentionBack);
        assertThat(postMentionBack.getMentionedUser()).isNull();
    }

    @Test
    void commentMentionsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        CommentMention commentMentionBack = getCommentMentionRandomSampleGenerator();

        userLite.addCommentMentions(commentMentionBack);
        assertThat(userLite.getCommentMentions()).containsOnly(commentMentionBack);
        assertThat(commentMentionBack.getMentionedUser()).isEqualTo(userLite);

        userLite.removeCommentMentions(commentMentionBack);
        assertThat(userLite.getCommentMentions()).doesNotContain(commentMentionBack);
        assertThat(commentMentionBack.getMentionedUser()).isNull();

        userLite.commentMentions(new HashSet<>(Set.of(commentMentionBack)));
        assertThat(userLite.getCommentMentions()).containsOnly(commentMentionBack);
        assertThat(commentMentionBack.getMentionedUser()).isEqualTo(userLite);

        userLite.setCommentMentions(new HashSet<>());
        assertThat(userLite.getCommentMentions()).doesNotContain(commentMentionBack);
        assertThat(commentMentionBack.getMentionedUser()).isNull();
    }

    @Test
    void chatRoomsTest() {
        UserLite userLite = getUserLiteRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        userLite.addChatRooms(chatRoomBack);
        assertThat(userLite.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getParticipants()).containsOnly(userLite);

        userLite.removeChatRooms(chatRoomBack);
        assertThat(userLite.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getParticipants()).doesNotContain(userLite);

        userLite.chatRooms(new HashSet<>(Set.of(chatRoomBack)));
        assertThat(userLite.getChatRooms()).containsOnly(chatRoomBack);
        assertThat(chatRoomBack.getParticipants()).containsOnly(userLite);

        userLite.setChatRooms(new HashSet<>());
        assertThat(userLite.getChatRooms()).doesNotContain(chatRoomBack);
        assertThat(chatRoomBack.getParticipants()).doesNotContain(userLite);
    }
}
