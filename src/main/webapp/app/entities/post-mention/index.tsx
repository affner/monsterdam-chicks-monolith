import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PostMention from './post-mention';
import PostMentionDetail from './post-mention-detail';
import PostMentionUpdate from './post-mention-update';
import PostMentionDeleteDialog from './post-mention-delete-dialog';

const PostMentionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PostMention />} />
    <Route path="new" element={<PostMentionUpdate />} />
    <Route path=":id">
      <Route index element={<PostMentionDetail />} />
      <Route path="edit" element={<PostMentionUpdate />} />
      <Route path="delete" element={<PostMentionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PostMentionRoutes;
