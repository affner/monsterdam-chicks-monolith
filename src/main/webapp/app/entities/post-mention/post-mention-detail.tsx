import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-mention.reducer';

export const PostMentionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postMentionEntity = useAppSelector(state => state.postMention.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postMentionDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.postMention.detail.title">PostMention</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postMentionEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.postMention.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postMentionEntity.createdDate ? (
              <TextFormat value={postMentionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.postMention.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postMentionEntity.lastModifiedDate ? (
              <TextFormat value={postMentionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.postMention.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postMentionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.postMention.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postMentionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.postMention.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {postMentionEntity.deletedDate ? (
              <TextFormat value={postMentionEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.postMention.originPost">Origin Post</Translate>
          </dt>
          <dd>{postMentionEntity.originPost ? postMentionEntity.originPost.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.postMention.mentionedUser">Mentioned User</Translate>
          </dt>
          <dd>{postMentionEntity.mentionedUser ? postMentionEntity.mentionedUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/post-mention" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-mention/${postMentionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostMentionDetail;
