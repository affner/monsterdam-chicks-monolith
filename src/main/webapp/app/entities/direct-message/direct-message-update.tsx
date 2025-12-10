import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDirectMessages } from 'app/entities/direct-message/direct-message.reducer';
import { getEntities as getChatRooms } from 'app/entities/chat-room/chat-room.reducer';
import { createEntity, getEntity, reset, updateEntity } from './direct-message.reducer';

export const DirectMessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const directMessages = useAppSelector(state => state.directMessage.entities);
  const chatRooms = useAppSelector(state => state.chatRoom.entities);
  const directMessageEntity = useAppSelector(state => state.directMessage.entity);
  const loading = useAppSelector(state => state.directMessage.loading);
  const updating = useAppSelector(state => state.directMessage.updating);
  const updateSuccess = useAppSelector(state => state.directMessage.updateSuccess);

  const handleClose = () => {
    navigate(`/direct-message${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDirectMessages({}));
    dispatch(getChatRooms({}));
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
    if (values.likeCount !== undefined && typeof values.likeCount !== 'number') {
      values.likeCount = Number(values.likeCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.repliedStoryId !== undefined && typeof values.repliedStoryId !== 'number') {
      values.repliedStoryId = Number(values.repliedStoryId);
    }
    if (values.senderId !== undefined && typeof values.senderId !== 'number') {
      values.senderId = Number(values.senderId);
    }

    const entity = {
      ...directMessageEntity,
      ...values,
      responseTo: directMessages.find(it => it.id.toString() === values.responseTo?.toString()),
      chatRoom: chatRooms.find(it => it.id.toString() === values.chatRoom?.toString()),
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
          ...directMessageEntity,
          readDate: convertDateTimeFromServer(directMessageEntity.readDate),
          createdDate: convertDateTimeFromServer(directMessageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(directMessageEntity.lastModifiedDate),
          responseTo: directMessageEntity?.responseTo?.id,
          chatRoom: directMessageEntity?.chatRoom?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.directMessage.home.createOrEditLabel" data-cy="DirectMessageCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.directMessage.home.createOrEditLabel">Create or edit a DirectMessage</Translate>
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
                  id="direct-message-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.messageContent')}
                id="direct-message-messageContent"
                name="messageContent"
                data-cy="messageContent"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.readDate')}
                id="direct-message-readDate"
                name="readDate"
                data-cy="readDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.likeCount')}
                id="direct-message-likeCount"
                name="likeCount"
                data-cy="likeCount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.isHidden')}
                id="direct-message-isHidden"
                name="isHidden"
                data-cy="isHidden"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.createdDate')}
                id="direct-message-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.lastModifiedDate')}
                id="direct-message-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.createdBy')}
                id="direct-message-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.lastModifiedBy')}
                id="direct-message-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.isDeleted')}
                id="direct-message-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.repliedStoryId')}
                id="direct-message-repliedStoryId"
                name="repliedStoryId"
                data-cy="repliedStoryId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.directMessage.senderId')}
                id="direct-message-senderId"
                name="senderId"
                data-cy="senderId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="direct-message-responseTo"
                name="responseTo"
                data-cy="responseTo"
                label={translate('monsterdamChicksApp.directMessage.responseTo')}
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
              <ValidatedField
                id="direct-message-chatRoom"
                name="chatRoom"
                data-cy="chatRoom"
                label={translate('monsterdamChicksApp.directMessage.chatRoom')}
                type="select"
              >
                <option value="" key="0" />
                {chatRooms
                  ? chatRooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/direct-message" replace color="info">
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

export default DirectMessageUpdate;
