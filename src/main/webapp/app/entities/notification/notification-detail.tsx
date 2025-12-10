import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification.reducer';

export const NotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificationEntity = useAppSelector(state => state.notification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.notification.detail.title">Notification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.id}</dd>
          <dt>
            <span id="readDate">
              <Translate contentKey="monsterdamChicksApp.notification.readDate">Read Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.readDate ? <TextFormat value={notificationEntity.readDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="notificationKind">
              <Translate contentKey="monsterdamChicksApp.notification.notificationKind">Notification Kind</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.notificationKind}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.notification.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.createdDate ? (
              <TextFormat value={notificationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.notification.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.lastModifiedDate ? (
              <TextFormat value={notificationEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.notification.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.notification.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.lastModifiedBy}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="monsterdamChicksApp.notification.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="commentedUserId">
              <Translate contentKey="monsterdamChicksApp.notification.commentedUserId">Commented User Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.commentedUserId}</dd>
          <dt>
            <span id="messagedUserId">
              <Translate contentKey="monsterdamChicksApp.notification.messagedUserId">Messaged User Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.messagedUserId}</dd>
          <dt>
            <span id="mentionerIdInPost">
              <Translate contentKey="monsterdamChicksApp.notification.mentionerIdInPost">Mentioner Id In Post</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.mentionerIdInPost}</dd>
          <dt>
            <span id="mentionerIdInComment">
              <Translate contentKey="monsterdamChicksApp.notification.mentionerIdInComment">Mentioner Id In Comment</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.mentionerIdInComment}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.notification.comment">Comment</Translate>
          </dt>
          <dd>{notificationEntity.comment ? notificationEntity.comment.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.notification.post">Post</Translate>
          </dt>
          <dd>{notificationEntity.post ? notificationEntity.post.id : ''}</dd>
          <dt>
            <Translate contentKey="monsterdamChicksApp.notification.message">Message</Translate>
          </dt>
          <dd>{notificationEntity.message ? notificationEntity.message.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationDetail;
