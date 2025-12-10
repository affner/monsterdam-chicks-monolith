import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedback.reducer';

export const FeedbackDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.feedback.detail.title">Feedback</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="monsterdamChicksApp.feedback.content">Content</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.content}</dd>
          <dt>
            <span id="feedbackDate">
              <Translate contentKey="monsterdamChicksApp.feedback.feedbackDate">Feedback Date</Translate>
            </span>
          </dt>
          <dd>
            {feedbackEntity.feedbackDate ? <TextFormat value={feedbackEntity.feedbackDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="feedbackRating">
              <Translate contentKey="monsterdamChicksApp.feedback.feedbackRating">Feedback Rating</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackRating}</dd>
          <dt>
            <span id="feedbackType">
              <Translate contentKey="monsterdamChicksApp.feedback.feedbackType">Feedback Type</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackType}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.feedback.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {feedbackEntity.createdDate ? <TextFormat value={feedbackEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.feedback.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {feedbackEntity.lastModifiedDate ? (
              <TextFormat value={feedbackEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.feedback.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.feedback.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.feedback.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.feedback.creator">Creator</Translate>
          </dt>
          <dd>{feedbackEntity.creator ? feedbackEntity.creator.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedback" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedback/${feedbackEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackDetail;
