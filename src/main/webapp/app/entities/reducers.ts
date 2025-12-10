import userLite from 'app/entities/user-lite/user-lite.reducer';
import userProfile from 'app/entities/user-profile/user-profile.reducer';
import userSettings from 'app/entities/user-settings/user-settings.reducer';
import userAssociation from 'app/entities/user-association/user-association.reducer';
import userEvent from 'app/entities/user-event/user-event.reducer';
import bookMark from 'app/entities/book-mark/book-mark.reducer';
import feedback from 'app/entities/feedback/feedback.reducer';
import personalSocialLinks from 'app/entities/personal-social-links/personal-social-links.reducer';
import stateUserRelation from 'app/entities/state-user-relation/state-user-relation.reducer';
import hashTag from 'app/entities/hash-tag/hash-tag.reducer';
import specialAward from 'app/entities/special-award/special-award.reducer';
import purchasedSubscription from 'app/entities/purchased-subscription/purchased-subscription.reducer';
import auction from 'app/entities/auction/auction.reducer';
import country from 'app/entities/country/country.reducer';
import state from 'app/entities/state/state.reducer';
import socialNetwork from 'app/entities/social-network/social-network.reducer';
import moneyWithdraw from 'app/entities/money-withdraw/money-withdraw.reducer';
import moneyEarning from 'app/entities/money-earning/money-earning.reducer';
import subscriptionBundle from 'app/entities/subscription-bundle/subscription-bundle.reducer';
import viewerWallet from 'app/entities/viewer-wallet/viewer-wallet.reducer';
import payment from 'app/entities/payment/payment.reducer';
import paymentMethod from 'app/entities/payment-method/payment-method.reducer';
import paymentProvider from 'app/entities/payment-provider/payment-provider.reducer';
import moneyGift from 'app/entities/money-gift/money-gift.reducer';
import taxInfo from 'app/entities/tax-info/tax-info.reducer';
import subscriptionPlanOffer from 'app/entities/subscription-plan-offer/subscription-plan-offer.reducer';
import purchasedContent from 'app/entities/purchased-content/purchased-content.reducer';
import trialLink from 'app/entities/trial-link/trial-link.reducer';
import singleAudio from 'app/entities/single-audio/single-audio.reducer';
import singleVideo from 'app/entities/single-video/single-video.reducer';
import singlePhoto from 'app/entities/single-photo/single-photo.reducer';
import videoStory from 'app/entities/video-story/video-story.reducer';
import contentPackage from 'app/entities/content-package/content-package.reducer';
import postFeed from 'app/entities/post-feed/post-feed.reducer';
import postComment from 'app/entities/post-comment/post-comment.reducer';
import postPoll from 'app/entities/post-poll/post-poll.reducer';
import chatRoom from 'app/entities/chat-room/chat-room.reducer';
import directMessage from 'app/entities/direct-message/direct-message.reducer';
import postMention from 'app/entities/post-mention/post-mention.reducer';
import commentMention from 'app/entities/comment-mention/comment-mention.reducer';
import likeMark from 'app/entities/like-mark/like-mark.reducer';
import pollVote from 'app/entities/poll-vote/poll-vote.reducer';
import pollOption from 'app/entities/poll-option/poll-option.reducer';
import notification from 'app/entities/notification/notification.reducer';
import platformAdminUser from 'app/entities/platform-admin-user/platform-admin-user.reducer';
import identityDocumentReview from 'app/entities/identity-document-review/identity-document-review.reducer';
import documentReviewObservation from 'app/entities/document-review-observation/document-review-observation.reducer';
import identityDocument from 'app/entities/identity-document/identity-document.reducer';
import assistanceTicket from 'app/entities/assistance-ticket/assistance-ticket.reducer';
import moderationAction from 'app/entities/moderation-action/moderation-action.reducer';
import adminAnnouncement from 'app/entities/admin-announcement/admin-announcement.reducer';
import helpCategory from 'app/entities/help-category/help-category.reducer';
import helpSubcategory from 'app/entities/help-subcategory/help-subcategory.reducer';
import helpQuestion from 'app/entities/help-question/help-question.reducer';
import helpRelatedArticle from 'app/entities/help-related-article/help-related-article.reducer';
import userReport from 'app/entities/user-report/user-report.reducer';
import adminEmailConfigs from 'app/entities/admin-email-configs/admin-email-configs.reducer';
import adminSystemConfigs from 'app/entities/admin-system-configs/admin-system-configs.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userLite,
  userProfile,
  userSettings,
  userAssociation,
  userEvent,
  bookMark,
  feedback,
  personalSocialLinks,
  stateUserRelation,
  hashTag,
  specialAward,
  purchasedSubscription,
  auction,
  country,
  state,
  socialNetwork,
  moneyWithdraw,
  moneyEarning,
  subscriptionBundle,
  viewerWallet,
  payment,
  paymentMethod,
  paymentProvider,
  moneyGift,
  taxInfo,
  subscriptionPlanOffer,
  purchasedContent,
  trialLink,
  singleAudio,
  singleVideo,
  singlePhoto,
  videoStory,
  contentPackage,
  postFeed,
  postComment,
  postPoll,
  chatRoom,
  directMessage,
  postMention,
  commentMention,
  likeMark,
  pollVote,
  pollOption,
  notification,
  platformAdminUser,
  identityDocumentReview,
  documentReviewObservation,
  identityDocument,
  assistanceTicket,
  moderationAction,
  adminAnnouncement,
  helpCategory,
  helpSubcategory,
  helpQuestion,
  helpRelatedArticle,
  userReport,
  adminEmailConfigs,
  adminSystemConfigs,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
