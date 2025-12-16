import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-comment.reducer';

export const PostCommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postCommentEntity = useAppSelector(state => state.postComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postCommentDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.postComment.detail.title">PostComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.id}</dd>
          <dt>
            <span id="commentContent">
              <Translate contentKey="monsterdamChicksApp.postComment.commentContent">Comment Content</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.commentContent}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="monsterdamChicksApp.postComment.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.postComment.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postCommentEntity.createdDate ? (
              <TextFormat value={postCommentEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.postComment.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postCommentEntity.lastModifiedDate ? (
              <TextFormat value={postCommentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.postComment.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.postComment.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postCommentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.postComment.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {postCommentEntity.deletedDate ? (
              <TextFormat value={postCommentEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.postComment.post">Post</Translate>
          </dt>
          <dd>{postCommentEntity.post ? postCommentEntity.post.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.postComment.responseTo">Response To</Translate>
          </dt>
          <dd>{postCommentEntity.responseTo ? postCommentEntity.responseTo.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.postComment.commenter">Commenter</Translate>
          </dt>
          <dd>{postCommentEntity.commenter ? postCommentEntity.commenter.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/post-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-comment/${postCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostCommentDetail;
