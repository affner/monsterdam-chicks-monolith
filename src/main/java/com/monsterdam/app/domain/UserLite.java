package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.UserGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A UserLite.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_lite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "thumbnail_s_3_key")
    private String thumbnailS3Key;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private UserGender gender;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @JsonIgnoreProperties(value = { "stateOfResidence", "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserProfile profile;

    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserSettings settings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "states" }, allowSetters = true)
    private Country countryOfBirth;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "viewer")
    @JsonIgnoreProperties(value = { "viewer", "creator", "payment", "subscriptionPlanOffer" }, allowSetters = true)
    private Set<PurchasedSubscription> purchasedSubscriptionsAsViewers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "viewer", "creator", "payment", "subscriptionPlanOffer" }, allowSetters = true)
    private Set<PurchasedSubscription> purchasedSubscriptionsAsCreators = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voter")
    @JsonIgnoreProperties(value = { "pollOption", "voter" }, allowSetters = true)
    private Set<PollVote> pollVotes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Set<UserAssociation> associationsAsOwners = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnoreProperties(value = { "assignedAdmin", "documentsReview", "user" }, allowSetters = true)
    private Set<AssistanceTicket> assistanceTickets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "viewer")
    @JsonIgnoreProperties(value = { "method", "provider", "viewer" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commenter")
    @JsonIgnoreProperties(value = { "post", "responseTo", "commenter" }, allowSetters = true)
    private Set<PostComment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "creator", "polls" }, allowSetters = true)
    private Set<PostFeed> posts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "contentPackage", "creator" }, allowSetters = true)
    private Set<SingleAudio> singleAudios = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "contentPackage", "creator" }, allowSetters = true)
    private Set<SingleVideo> singleVideos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "contentPackage", "creator" }, allowSetters = true)
    private Set<SinglePhoto> singlePhotos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnoreProperties(value = { "creator" }, allowSetters = true)
    private Set<VideoStory> videoStories = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender")
    @JsonIgnoreProperties(value = { "responseTo", "chatRoom", "sender" }, allowSetters = true)
    private Set<DirectMessage> directMessagesSents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "liker")
    @JsonIgnoreProperties(value = { "liker" }, allowSetters = true)
    private Set<LikeMark> likeMarks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipient")
    @JsonIgnoreProperties(value = { "comment", "post", "message", "recipient", "actor" }, allowSetters = true)
    private Set<Notification> notificationsReceiveds = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "actor")
    @JsonIgnoreProperties(value = { "comment", "post", "message", "recipient", "actor" }, allowSetters = true)
    private Set<Notification> notificationsTriggereds = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentionedUser")
    @JsonIgnoreProperties(value = { "originPost", "mentionedUser" }, allowSetters = true)
    private Set<PostMention> postMentions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mentionedUser")
    @JsonIgnoreProperties(value = { "originPostComment", "mentionedUser" }, allowSetters = true)
    private Set<CommentMention> commentMentions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @JsonIgnoreProperties(value = { "participants" }, allowSetters = true)
    private Set<ChatRoom> chatRooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserLite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnailS3Key() {
        return this.thumbnailS3Key;
    }

    public UserLite thumbnailS3Key(String thumbnailS3Key) {
        this.setThumbnailS3Key(thumbnailS3Key);
        return this;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public UserLite birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserGender getGender() {
        return this.gender;
    }

    public UserLite gender(UserGender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserLite createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserLite lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserLite createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserLite lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public UserLite deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getNickName() {
        return this.nickName;
    }

    public UserLite nickName(String nickName) {
        this.setNickName(nickName);
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public UserLite fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserProfile getProfile() {
        return this.profile;
    }

    public void setProfile(UserProfile userProfile) {
        this.profile = userProfile;
    }

    public UserLite profile(UserProfile userProfile) {
        this.setProfile(userProfile);
        return this;
    }

    public UserSettings getSettings() {
        return this.settings;
    }

    public void setSettings(UserSettings userSettings) {
        this.settings = userSettings;
    }

    public UserLite settings(UserSettings userSettings) {
        this.setSettings(userSettings);
        return this;
    }

    public Country getCountryOfBirth() {
        return this.countryOfBirth;
    }

    public void setCountryOfBirth(Country country) {
        this.countryOfBirth = country;
    }

    public UserLite countryOfBirth(Country country) {
        this.setCountryOfBirth(country);
        return this;
    }

    public Set<PurchasedSubscription> getPurchasedSubscriptionsAsViewers() {
        return this.purchasedSubscriptionsAsViewers;
    }

    public void setPurchasedSubscriptionsAsViewers(Set<PurchasedSubscription> purchasedSubscriptions) {
        if (this.purchasedSubscriptionsAsViewers != null) {
            this.purchasedSubscriptionsAsViewers.forEach(i -> i.setViewer(null));
        }
        if (purchasedSubscriptions != null) {
            purchasedSubscriptions.forEach(i -> i.setViewer(this));
        }
        this.purchasedSubscriptionsAsViewers = purchasedSubscriptions;
    }

    public UserLite purchasedSubscriptionsAsViewers(Set<PurchasedSubscription> purchasedSubscriptions) {
        this.setPurchasedSubscriptionsAsViewers(purchasedSubscriptions);
        return this;
    }

    public UserLite addPurchasedSubscriptionsAsViewer(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptionsAsViewers.add(purchasedSubscription);
        purchasedSubscription.setViewer(this);
        return this;
    }

    public UserLite removePurchasedSubscriptionsAsViewer(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptionsAsViewers.remove(purchasedSubscription);
        purchasedSubscription.setViewer(null);
        return this;
    }

    public Set<PurchasedSubscription> getPurchasedSubscriptionsAsCreators() {
        return this.purchasedSubscriptionsAsCreators;
    }

    public void setPurchasedSubscriptionsAsCreators(Set<PurchasedSubscription> purchasedSubscriptions) {
        if (this.purchasedSubscriptionsAsCreators != null) {
            this.purchasedSubscriptionsAsCreators.forEach(i -> i.setCreator(null));
        }
        if (purchasedSubscriptions != null) {
            purchasedSubscriptions.forEach(i -> i.setCreator(this));
        }
        this.purchasedSubscriptionsAsCreators = purchasedSubscriptions;
    }

    public UserLite purchasedSubscriptionsAsCreators(Set<PurchasedSubscription> purchasedSubscriptions) {
        this.setPurchasedSubscriptionsAsCreators(purchasedSubscriptions);
        return this;
    }

    public UserLite addPurchasedSubscriptionsAsCreator(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptionsAsCreators.add(purchasedSubscription);
        purchasedSubscription.setCreator(this);
        return this;
    }

    public UserLite removePurchasedSubscriptionsAsCreator(PurchasedSubscription purchasedSubscription) {
        this.purchasedSubscriptionsAsCreators.remove(purchasedSubscription);
        purchasedSubscription.setCreator(null);
        return this;
    }

    public Set<PollVote> getPollVotes() {
        return this.pollVotes;
    }

    public void setPollVotes(Set<PollVote> pollVotes) {
        if (this.pollVotes != null) {
            this.pollVotes.forEach(i -> i.setVoter(null));
        }
        if (pollVotes != null) {
            pollVotes.forEach(i -> i.setVoter(this));
        }
        this.pollVotes = pollVotes;
    }

    public UserLite pollVotes(Set<PollVote> pollVotes) {
        this.setPollVotes(pollVotes);
        return this;
    }

    public UserLite addPollVotes(PollVote pollVote) {
        this.pollVotes.add(pollVote);
        pollVote.setVoter(this);
        return this;
    }

    public UserLite removePollVotes(PollVote pollVote) {
        this.pollVotes.remove(pollVote);
        pollVote.setVoter(null);
        return this;
    }

    public Set<UserAssociation> getAssociationsAsOwners() {
        return this.associationsAsOwners;
    }

    public void setAssociationsAsOwners(Set<UserAssociation> userAssociations) {
        if (this.associationsAsOwners != null) {
            this.associationsAsOwners.forEach(i -> i.setOwner(null));
        }
        if (userAssociations != null) {
            userAssociations.forEach(i -> i.setOwner(this));
        }
        this.associationsAsOwners = userAssociations;
    }

    public UserLite associationsAsOwners(Set<UserAssociation> userAssociations) {
        this.setAssociationsAsOwners(userAssociations);
        return this;
    }

    public UserLite addAssociationsAsOwner(UserAssociation userAssociation) {
        this.associationsAsOwners.add(userAssociation);
        userAssociation.setOwner(this);
        return this;
    }

    public UserLite removeAssociationsAsOwner(UserAssociation userAssociation) {
        this.associationsAsOwners.remove(userAssociation);
        userAssociation.setOwner(null);
        return this;
    }

    public Set<AssistanceTicket> getAssistanceTickets() {
        return this.assistanceTickets;
    }

    public void setAssistanceTickets(Set<AssistanceTicket> assistanceTickets) {
        if (this.assistanceTickets != null) {
            this.assistanceTickets.forEach(i -> i.setUser(null));
        }
        if (assistanceTickets != null) {
            assistanceTickets.forEach(i -> i.setUser(this));
        }
        this.assistanceTickets = assistanceTickets;
    }

    public UserLite assistanceTickets(Set<AssistanceTicket> assistanceTickets) {
        this.setAssistanceTickets(assistanceTickets);
        return this;
    }

    public UserLite addAssistanceTickets(AssistanceTicket assistanceTicket) {
        this.assistanceTickets.add(assistanceTicket);
        assistanceTicket.setUser(this);
        return this;
    }

    public UserLite removeAssistanceTickets(AssistanceTicket assistanceTicket) {
        this.assistanceTickets.remove(assistanceTicket);
        assistanceTicket.setUser(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setViewer(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setViewer(this));
        }
        this.payments = payments;
    }

    public UserLite payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public UserLite addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setViewer(this);
        return this;
    }

    public UserLite removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setViewer(null);
        return this;
    }

    public Set<PostComment> getComments() {
        return this.comments;
    }

    public void setComments(Set<PostComment> postComments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setCommenter(null));
        }
        if (postComments != null) {
            postComments.forEach(i -> i.setCommenter(this));
        }
        this.comments = postComments;
    }

    public UserLite comments(Set<PostComment> postComments) {
        this.setComments(postComments);
        return this;
    }

    public UserLite addComments(PostComment postComment) {
        this.comments.add(postComment);
        postComment.setCommenter(this);
        return this;
    }

    public UserLite removeComments(PostComment postComment) {
        this.comments.remove(postComment);
        postComment.setCommenter(null);
        return this;
    }

    public Set<PostFeed> getPosts() {
        return this.posts;
    }

    public void setPosts(Set<PostFeed> postFeeds) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setCreator(null));
        }
        if (postFeeds != null) {
            postFeeds.forEach(i -> i.setCreator(this));
        }
        this.posts = postFeeds;
    }

    public UserLite posts(Set<PostFeed> postFeeds) {
        this.setPosts(postFeeds);
        return this;
    }

    public UserLite addPosts(PostFeed postFeed) {
        this.posts.add(postFeed);
        postFeed.setCreator(this);
        return this;
    }

    public UserLite removePosts(PostFeed postFeed) {
        this.posts.remove(postFeed);
        postFeed.setCreator(null);
        return this;
    }

    public Set<SingleAudio> getSingleAudios() {
        return this.singleAudios;
    }

    public void setSingleAudios(Set<SingleAudio> singleAudios) {
        if (this.singleAudios != null) {
            this.singleAudios.forEach(i -> i.setCreator(null));
        }
        if (singleAudios != null) {
            singleAudios.forEach(i -> i.setCreator(this));
        }
        this.singleAudios = singleAudios;
    }

    public UserLite singleAudios(Set<SingleAudio> singleAudios) {
        this.setSingleAudios(singleAudios);
        return this;
    }

    public UserLite addSingleAudios(SingleAudio singleAudio) {
        this.singleAudios.add(singleAudio);
        singleAudio.setCreator(this);
        return this;
    }

    public UserLite removeSingleAudios(SingleAudio singleAudio) {
        this.singleAudios.remove(singleAudio);
        singleAudio.setCreator(null);
        return this;
    }

    public Set<SingleVideo> getSingleVideos() {
        return this.singleVideos;
    }

    public void setSingleVideos(Set<SingleVideo> singleVideos) {
        if (this.singleVideos != null) {
            this.singleVideos.forEach(i -> i.setCreator(null));
        }
        if (singleVideos != null) {
            singleVideos.forEach(i -> i.setCreator(this));
        }
        this.singleVideos = singleVideos;
    }

    public UserLite singleVideos(Set<SingleVideo> singleVideos) {
        this.setSingleVideos(singleVideos);
        return this;
    }

    public UserLite addSingleVideos(SingleVideo singleVideo) {
        this.singleVideos.add(singleVideo);
        singleVideo.setCreator(this);
        return this;
    }

    public UserLite removeSingleVideos(SingleVideo singleVideo) {
        this.singleVideos.remove(singleVideo);
        singleVideo.setCreator(null);
        return this;
    }

    public Set<SinglePhoto> getSinglePhotos() {
        return this.singlePhotos;
    }

    public void setSinglePhotos(Set<SinglePhoto> singlePhotos) {
        if (this.singlePhotos != null) {
            this.singlePhotos.forEach(i -> i.setCreator(null));
        }
        if (singlePhotos != null) {
            singlePhotos.forEach(i -> i.setCreator(this));
        }
        this.singlePhotos = singlePhotos;
    }

    public UserLite singlePhotos(Set<SinglePhoto> singlePhotos) {
        this.setSinglePhotos(singlePhotos);
        return this;
    }

    public UserLite addSinglePhotos(SinglePhoto singlePhoto) {
        this.singlePhotos.add(singlePhoto);
        singlePhoto.setCreator(this);
        return this;
    }

    public UserLite removeSinglePhotos(SinglePhoto singlePhoto) {
        this.singlePhotos.remove(singlePhoto);
        singlePhoto.setCreator(null);
        return this;
    }

    public Set<VideoStory> getVideoStories() {
        return this.videoStories;
    }

    public void setVideoStories(Set<VideoStory> videoStories) {
        if (this.videoStories != null) {
            this.videoStories.forEach(i -> i.setCreator(null));
        }
        if (videoStories != null) {
            videoStories.forEach(i -> i.setCreator(this));
        }
        this.videoStories = videoStories;
    }

    public UserLite videoStories(Set<VideoStory> videoStories) {
        this.setVideoStories(videoStories);
        return this;
    }

    public UserLite addVideoStories(VideoStory videoStory) {
        this.videoStories.add(videoStory);
        videoStory.setCreator(this);
        return this;
    }

    public UserLite removeVideoStories(VideoStory videoStory) {
        this.videoStories.remove(videoStory);
        videoStory.setCreator(null);
        return this;
    }

    public Set<DirectMessage> getDirectMessagesSents() {
        return this.directMessagesSents;
    }

    public void setDirectMessagesSents(Set<DirectMessage> directMessages) {
        if (this.directMessagesSents != null) {
            this.directMessagesSents.forEach(i -> i.setSender(null));
        }
        if (directMessages != null) {
            directMessages.forEach(i -> i.setSender(this));
        }
        this.directMessagesSents = directMessages;
    }

    public UserLite directMessagesSents(Set<DirectMessage> directMessages) {
        this.setDirectMessagesSents(directMessages);
        return this;
    }

    public UserLite addDirectMessagesSent(DirectMessage directMessage) {
        this.directMessagesSents.add(directMessage);
        directMessage.setSender(this);
        return this;
    }

    public UserLite removeDirectMessagesSent(DirectMessage directMessage) {
        this.directMessagesSents.remove(directMessage);
        directMessage.setSender(null);
        return this;
    }

    public Set<LikeMark> getLikeMarks() {
        return this.likeMarks;
    }

    public void setLikeMarks(Set<LikeMark> likeMarks) {
        if (this.likeMarks != null) {
            this.likeMarks.forEach(i -> i.setLiker(null));
        }
        if (likeMarks != null) {
            likeMarks.forEach(i -> i.setLiker(this));
        }
        this.likeMarks = likeMarks;
    }

    public UserLite likeMarks(Set<LikeMark> likeMarks) {
        this.setLikeMarks(likeMarks);
        return this;
    }

    public UserLite addLikeMarks(LikeMark likeMark) {
        this.likeMarks.add(likeMark);
        likeMark.setLiker(this);
        return this;
    }

    public UserLite removeLikeMarks(LikeMark likeMark) {
        this.likeMarks.remove(likeMark);
        likeMark.setLiker(null);
        return this;
    }

    public Set<Notification> getNotificationsReceiveds() {
        return this.notificationsReceiveds;
    }

    public void setNotificationsReceiveds(Set<Notification> notifications) {
        if (this.notificationsReceiveds != null) {
            this.notificationsReceiveds.forEach(i -> i.setRecipient(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setRecipient(this));
        }
        this.notificationsReceiveds = notifications;
    }

    public UserLite notificationsReceiveds(Set<Notification> notifications) {
        this.setNotificationsReceiveds(notifications);
        return this;
    }

    public UserLite addNotificationsReceived(Notification notification) {
        this.notificationsReceiveds.add(notification);
        notification.setRecipient(this);
        return this;
    }

    public UserLite removeNotificationsReceived(Notification notification) {
        this.notificationsReceiveds.remove(notification);
        notification.setRecipient(null);
        return this;
    }

    public Set<Notification> getNotificationsTriggereds() {
        return this.notificationsTriggereds;
    }

    public void setNotificationsTriggereds(Set<Notification> notifications) {
        if (this.notificationsTriggereds != null) {
            this.notificationsTriggereds.forEach(i -> i.setActor(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setActor(this));
        }
        this.notificationsTriggereds = notifications;
    }

    public UserLite notificationsTriggereds(Set<Notification> notifications) {
        this.setNotificationsTriggereds(notifications);
        return this;
    }

    public UserLite addNotificationsTriggered(Notification notification) {
        this.notificationsTriggereds.add(notification);
        notification.setActor(this);
        return this;
    }

    public UserLite removeNotificationsTriggered(Notification notification) {
        this.notificationsTriggereds.remove(notification);
        notification.setActor(null);
        return this;
    }

    public Set<PostMention> getPostMentions() {
        return this.postMentions;
    }

    public void setPostMentions(Set<PostMention> postMentions) {
        if (this.postMentions != null) {
            this.postMentions.forEach(i -> i.setMentionedUser(null));
        }
        if (postMentions != null) {
            postMentions.forEach(i -> i.setMentionedUser(this));
        }
        this.postMentions = postMentions;
    }

    public UserLite postMentions(Set<PostMention> postMentions) {
        this.setPostMentions(postMentions);
        return this;
    }

    public UserLite addPostMentions(PostMention postMention) {
        this.postMentions.add(postMention);
        postMention.setMentionedUser(this);
        return this;
    }

    public UserLite removePostMentions(PostMention postMention) {
        this.postMentions.remove(postMention);
        postMention.setMentionedUser(null);
        return this;
    }

    public Set<CommentMention> getCommentMentions() {
        return this.commentMentions;
    }

    public void setCommentMentions(Set<CommentMention> commentMentions) {
        if (this.commentMentions != null) {
            this.commentMentions.forEach(i -> i.setMentionedUser(null));
        }
        if (commentMentions != null) {
            commentMentions.forEach(i -> i.setMentionedUser(this));
        }
        this.commentMentions = commentMentions;
    }

    public UserLite commentMentions(Set<CommentMention> commentMentions) {
        this.setCommentMentions(commentMentions);
        return this;
    }

    public UserLite addCommentMentions(CommentMention commentMention) {
        this.commentMentions.add(commentMention);
        commentMention.setMentionedUser(this);
        return this;
    }

    public UserLite removeCommentMentions(CommentMention commentMention) {
        this.commentMentions.remove(commentMention);
        commentMention.setMentionedUser(null);
        return this;
    }

    public Set<ChatRoom> getChatRooms() {
        return this.chatRooms;
    }

    public void setChatRooms(Set<ChatRoom> chatRooms) {
        if (this.chatRooms != null) {
            this.chatRooms.forEach(i -> i.removeParticipants(this));
        }
        if (chatRooms != null) {
            chatRooms.forEach(i -> i.addParticipants(this));
        }
        this.chatRooms = chatRooms;
    }

    public UserLite chatRooms(Set<ChatRoom> chatRooms) {
        this.setChatRooms(chatRooms);
        return this;
    }

    public UserLite addChatRooms(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
        chatRoom.getParticipants().add(this);
        return this;
    }

    public UserLite removeChatRooms(ChatRoom chatRoom) {
        this.chatRooms.remove(chatRoom);
        chatRoom.getParticipants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLite)) {
            return false;
        }
        return getId() != null && getId().equals(((UserLite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLite{" +
            "id=" + getId() +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", fullName='" + getFullName() + "'" +
            "}";
    }
}
