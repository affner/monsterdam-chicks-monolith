import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPostComments } from 'app/entities/post-comment/post-comment.reducer';
import { getEntities as getPostFeeds } from 'app/entities/post-feed/post-feed.reducer';
import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { NotificationKind } from 'app/shared/model/enumerations/notification-kind.model';
import { createEntity, getEntity, reset, updateEntity } from './notification.reducer';

export const NotificationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const postComments = useAppSelector(state => state.postComment.entities);
  const postFeeds = useAppSelector(state => state.postFeed.entities);
  const directMessages = useAppSelector(state => state.directMessage.entities);
  const notificationEntity = useAppSelector(state => state.notification.entity);
  const loading = useAppSelector(state => state.notification.loading);
  const updating = useAppSelector(state => state.notification.updating);
  const updateSuccess = useAppSelector(state => state.notification.updateSuccess);
  const notificationKindValues = Object.keys(NotificationKind);

  const handleClose = () => {
    navigate(`/notification${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPostComments({}));
    dispatch(getPostFeeds({}));
    dispatch(getDirectMessages({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.readDate = convertDateTimeToServer(values.readDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.commentedUserId !== undefined && typeof values.commentedUserId !== 'number') {
      values.commentedUserId = Number(values.commentedUserId);
    }
    if (values.messagedUserId !== undefined && typeof values.messagedUserId !== 'number') {
      values.messagedUserId = Number(values.messagedUserId);
    }
    if (values.mentionerIdInPost !== undefined && typeof values.mentionerIdInPost !== 'number') {
      values.mentionerIdInPost = Number(values.mentionerIdInPost);
    }
    if (values.mentionerIdInComment !== undefined && typeof values.mentionerIdInComment !== 'number') {
      values.mentionerIdInComment = Number(values.mentionerIdInComment);
    }

    const entity = {
      ...notificationEntity,
      ...values,
      comment: postComments.find(it => it.id.toString() === values.comment?.toString()),
      post: postFeeds.find(it => it.id.toString() === values.post?.toString()),
      message: directMessages.find(it => it.id.toString() === values.message?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          readDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          notificationKind: 'COMMENT',
          ...notificationEntity,
          readDate: convertDateTimeFromServer(notificationEntity.readDate),
          createdDate: convertDateTimeFromServer(notificationEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(notificationEntity.lastModifiedDate),
          comment: notificationEntity?.comment?.id,
          post: notificationEntity?.post?.id,
          message: notificationEntity?.message?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.notification.home.createOrEditLabel" data-cy="NotificationCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.notification.home.createOrEditLabel">Create or edit a Notification</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="notification-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.readDate')}
                id="notification-readDate"
                name="readDate"
                data-cy="readDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.notificationKind')}
                id="notification-notificationKind"
                name="notificationKind"
                data-cy="notificationKind"
                type="select"
              >
                {notificationKindValues.map(notificationKind => (
                  <option value={notificationKind} key={notificationKind}>
                    {translate(`monsterdamChicksApp.NotificationKind.${notificationKind}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.createdDate')}
                id="notification-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.lastModifiedDate')}
                id="notification-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.createdBy')}
                id="notification-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.lastModifiedBy')}
                id="notification-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.isDeleted')}
                id="notification-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.commentedUserId')}
                id="notification-commentedUserId"
                name="commentedUserId"
                data-cy="commentedUserId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.messagedUserId')}
                id="notification-messagedUserId"
                name="messagedUserId"
                data-cy="messagedUserId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.mentionerIdInPost')}
                id="notification-mentionerIdInPost"
                name="mentionerIdInPost"
                data-cy="mentionerIdInPost"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.notification.mentionerIdInComment')}
                id="notification-mentionerIdInComment"
                name="mentionerIdInComment"
                data-cy="mentionerIdInComment"
                type="text"
              />
              <ValidatedField
                id="notification-comment"
                name="comment"
                data-cy="comment"
                label={translate('monsterdamChicksApp.notification.comment')}
                type="select"
              >
                <option value="" key="0" />
                {postComments
                  ? postComments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="notification-post"
                name="post"
                data-cy="post"
                label={translate('monsterdamChicksApp.notification.post')}
                type="select"
              >
                <option value="" key="0" />
                {postFeeds
                  ? postFeeds.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="notification-message"
                name="message"
                data-cy="message"
                label={translate('monsterdamChicksApp.notification.message')}
                type="select"
              >
                <option value="" key="0" />
                {directMessages
                  ? directMessages.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NotificationUpdate;
