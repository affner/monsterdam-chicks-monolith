import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CommentMention from './comment-mention';
import CommentMentionDetail from './comment-mention-detail';
import CommentMentionUpdate from './comment-mention-update';
import CommentMentionDeleteDialog from './comment-mention-delete-dialog';

const CommentMentionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CommentMention />} />
    <Route path="new" element={<CommentMentionUpdate />} />
    <Route path=":id">
      <Route index element={<CommentMentionDetail />} />
      <Route path="edit" element={<CommentMentionUpdate />} />
      <Route path="delete" element={<CommentMentionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommentMentionRoutes;
