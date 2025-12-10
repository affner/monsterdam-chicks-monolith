import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-feed.reducer';

export const PostFeedDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postFeedEntity = useAppSelector(state => state.postFeed.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postFeedDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.postFeed.detail.title">PostFeed</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.id}</dd>
          <dt>
            <span id="postContent">
              <Translate contentKey="monsterdamChicksApp.postFeed.postContent">Post Content</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.postContent}</dd>
          <dt>
            <span id="isHidden">
              <Translate contentKey="monsterdamChicksApp.postFeed.isHidden">Is Hidden</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.isHidden ? 'true' : 'false'}</dd>
          <dt>
            <span id="pinnedPost">
              <Translate contentKey="monsterdamChicksApp.postFeed.pinnedPost">Pinned Post</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.pinnedPost ? 'true' : 'false'}</dd>
          <dt>
            <span id="likeCount">
              <Translate contentKey="monsterdamChicksApp.postFeed.likeCount">Like Count</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.likeCount}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.postFeed.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postFeedEntity.createdDate ? <TextFormat value={postFeedEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.postFeed.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postFeedEntity.lastModifiedDate ? (
              <TextFormat value={postFeedEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.postFeed.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.postFeed.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.postFeed.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{postFeedEntity.isDeleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/post-feed" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-feed/${postFeedEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostFeedDetail;
