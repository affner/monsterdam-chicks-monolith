import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserLite from './user-lite';
import UserProfile from './user-profile';
import UserSettings from './user-settings';
import UserAssociation from './user-association';
import UserEvent from './user-event';
import BookMark from './book-mark';
import Feedback from './feedback';
import PersonalSocialLinks from './personal-social-links';
import StateUserRelation from './state-user-relation';
import HashTag from './hash-tag';
import SpecialAward from './special-award';
import PurchasedSubscription from './purchased-subscription';
import Auction from './auction';
import Country from './country';
import State from './state';
import SocialNetwork from './social-network';
import MoneyWithdraw from './money-withdraw';
import MoneyEarning from './money-earning';
import SubscriptionBundle from './subscription-bundle';
import ViewerWallet from './viewer-wallet';
import Payment from './payment';
import PaymentMethod from './payment-method';
import PaymentProvider from './payment-provider';
import MoneyGift from './money-gift';
import TaxInfo from './tax-info';
import SubscriptionPlanOffer from './subscription-plan-offer';
import PurchasedContent from './purchased-content';
import TrialLink from './trial-link';
import SingleAudio from './single-audio';
import SingleVideo from './single-video';
import SinglePhoto from './single-photo';
import VideoStory from './video-story';
import ContentPackage from './content-package';
import PostFeed from './post-feed';
import PostComment from './post-comment';
import PostPoll from './post-poll';
import ChatRoom from './chat-room';
import DirectMessage from './direct-message';
import PostMention from './post-mention';
import CommentMention from './comment-mention';
import LikeMark from './like-mark';
import PollVote from './poll-vote';
import PollOption from './poll-option';
import Notification from './notification';
import PlatformAdminUser from './platform-admin-user';
import IdentityDocumentReview from './identity-document-review';
import DocumentReviewObservation from './document-review-observation';
import IdentityDocument from './identity-document';
import AssistanceTicket from './assistance-ticket';
import ModerationAction from './moderation-action';
import AdminAnnouncement from './admin-announcement';
import HelpCategory from './help-category';
import HelpSubcategory from './help-subcategory';
import HelpQuestion from './help-question';
import HelpRelatedArticle from './help-related-article';
import UserReport from './user-report';
import AdminEmailConfigs from './admin-email-configs';
import AdminSystemConfigs from './admin-system-configs';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-lite/*" element={<UserLite />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="user-settings/*" element={<UserSettings />} />
        <Route path="user-association/*" element={<UserAssociation />} />
        <Route path="user-event/*" element={<UserEvent />} />
        <Route path="book-mark/*" element={<BookMark />} />
        <Route path="feedback/*" element={<Feedback />} />
        <Route path="personal-social-links/*" element={<PersonalSocialLinks />} />
        <Route path="state-user-relation/*" element={<StateUserRelation />} />
        <Route path="hash-tag/*" element={<HashTag />} />
        <Route path="special-award/*" element={<SpecialAward />} />
        <Route path="purchased-subscription/*" element={<PurchasedSubscription />} />
        <Route path="auction/*" element={<Auction />} />
        <Route path="country/*" element={<Country />} />
        <Route path="state/*" element={<State />} />
        <Route path="social-network/*" element={<SocialNetwork />} />
        <Route path="money-withdraw/*" element={<MoneyWithdraw />} />
        <Route path="money-earning/*" element={<MoneyEarning />} />
        <Route path="subscription-bundle/*" element={<SubscriptionBundle />} />
        <Route path="viewer-wallet/*" element={<ViewerWallet />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="payment-method/*" element={<PaymentMethod />} />
        <Route path="payment-provider/*" element={<PaymentProvider />} />
        <Route path="money-gift/*" element={<MoneyGift />} />
        <Route path="tax-info/*" element={<TaxInfo />} />
        <Route path="subscription-plan-offer/*" element={<SubscriptionPlanOffer />} />
        <Route path="purchased-content/*" element={<PurchasedContent />} />
        <Route path="trial-link/*" element={<TrialLink />} />
        <Route path="single-audio/*" element={<SingleAudio />} />
        <Route path="single-video/*" element={<SingleVideo />} />
        <Route path="single-photo/*" element={<SinglePhoto />} />
        <Route path="video-story/*" element={<VideoStory />} />
        <Route path="content-package/*" element={<ContentPackage />} />
        <Route path="post-feed/*" element={<PostFeed />} />
        <Route path="post-comment/*" element={<PostComment />} />
        <Route path="post-poll/*" element={<PostPoll />} />
        <Route path="chat-room/*" element={<ChatRoom />} />
        <Route path="direct-message/*" element={<DirectMessage />} />
        <Route path="post-mention/*" element={<PostMention />} />
        <Route path="comment-mention/*" element={<CommentMention />} />
        <Route path="like-mark/*" element={<LikeMark />} />
        <Route path="poll-vote/*" element={<PollVote />} />
        <Route path="poll-option/*" element={<PollOption />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="platform-admin-user/*" element={<PlatformAdminUser />} />
        <Route path="identity-document-review/*" element={<IdentityDocumentReview />} />
        <Route path="document-review-observation/*" element={<DocumentReviewObservation />} />
        <Route path="identity-document/*" element={<IdentityDocument />} />
        <Route path="assistance-ticket/*" element={<AssistanceTicket />} />
        <Route path="moderation-action/*" element={<ModerationAction />} />
        <Route path="admin-announcement/*" element={<AdminAnnouncement />} />
        <Route path="help-category/*" element={<HelpCategory />} />
        <Route path="help-subcategory/*" element={<HelpSubcategory />} />
        <Route path="help-question/*" element={<HelpQuestion />} />
        <Route path="help-related-article/*" element={<HelpRelatedArticle />} />
        <Route path="user-report/*" element={<UserReport />} />
        <Route path="admin-email-configs/*" element={<AdminEmailConfigs />} />
        <Route path="admin-system-configs/*" element={<AdminSystemConfigs />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
