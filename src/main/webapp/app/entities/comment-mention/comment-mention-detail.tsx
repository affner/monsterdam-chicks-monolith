import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comment-mention.reducer';

export const CommentMentionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commentMentionEntity = useAppSelector(state => state.commentMention.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentMentionDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.commentMention.detail.title">CommentMention</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentMentionEntity.id}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.commentMention.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {commentMentionEntity.createdDate ? (
              <TextFormat value={commentMentionEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.commentMention.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {commentMentionEntity.lastModifiedDate ? (
              <TextFormat value={commentMentionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.commentMention.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{commentMentionEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.commentMention.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{commentMentionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.commentMention.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {commentMentionEntity.deletedDate ? (
              <TextFormat value={commentMentionEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.commentMention.originPostComment">Origin Post Comment</Translate>
          </dt>
          <dd>{commentMentionEntity.originPostComment ? commentMentionEntity.originPostComment.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.commentMention.mentionedUser">Mentioned User</Translate>
          </dt>
          <dd>{commentMentionEntity.mentionedUser ? commentMentionEntity.mentionedUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comment-mention" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comment-mention/${commentMentionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentMentionDetail;
