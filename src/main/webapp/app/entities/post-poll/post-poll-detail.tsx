import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-poll.reducer';

export const PostPollDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postPollEntity = useAppSelector(state => state.postPoll.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postPollDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.postPoll.detail.title">PostPoll</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.id}</dd>
          <dt>
            <span id="question">
              <Translate contentKey="monsterdamChicksApp.postPoll.question">Question</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.question}</dd>
          <dt>
            <span id="isMultiChoice">
              <Translate contentKey="monsterdamChicksApp.postPoll.isMultiChoice">Is Multi Choice</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.isMultiChoice ? 'true' : 'false'}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="monsterdamChicksApp.postPoll.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {postPollEntity.endDate ? <TextFormat value={postPollEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="duration">
              <Translate contentKey="monsterdamChicksApp.postPoll.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {postPollEntity.duration ? <DurationFormat value={postPollEntity.duration} /> : null} ({postPollEntity.duration})
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.postPoll.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {postPollEntity.createdDate ? <TextFormat value={postPollEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.postPoll.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {postPollEntity.lastModifiedDate ? (
              <TextFormat value={postPollEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.postPoll.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.postPoll.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.postPoll.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="monsterdamChicksApp.postPoll.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{postPollEntity.postId}</dd>
        </dl>
        <Button tag={Link} to="/post-poll" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-poll/${postPollEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostPollDetail;
