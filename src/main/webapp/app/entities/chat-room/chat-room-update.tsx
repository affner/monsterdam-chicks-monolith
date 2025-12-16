import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { ChatRoomType } from 'app/shared/model/enumerations/chat-room-type.model';
import { createEntity, getEntity, reset, updateEntity } from './chat-room.reducer';

export const ChatRoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const chatRoomEntity = useAppSelector(state => state.chatRoom.entity);
  const loading = useAppSelector(state => state.chatRoom.loading);
  const updating = useAppSelector(state => state.chatRoom.updating);
  const updateSuccess = useAppSelector(state => state.chatRoom.updateSuccess);
  const chatRoomTypeValues = Object.keys(ChatRoomType);

  const handleClose = () => {
    navigate(`/chat-room${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
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
    values.lastConnectionDate = convertDateTimeToServer(values.lastConnectionDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...chatRoomEntity,
      ...values,
      participants: mapIdList(values.participants),
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
          lastConnectionDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          roomType: 'DIRECT',
          ...chatRoomEntity,
          lastConnectionDate: convertDateTimeFromServer(chatRoomEntity.lastConnectionDate),
          createdDate: convertDateTimeFromServer(chatRoomEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(chatRoomEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(chatRoomEntity.deletedDate),
          participants: chatRoomEntity?.participants?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.chatRoom.home.createOrEditLabel" data-cy="ChatRoomCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.chatRoom.home.createOrEditLabel">Create or edit a ChatRoom</Translate>
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
                  id="chat-room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.roomType')}
                id="chat-room-roomType"
                name="roomType"
                data-cy="roomType"
                type="select"
              >
                {chatRoomTypeValues.map(chatRoomType => (
                  <option value={chatRoomType} key={chatRoomType}>
                    {translate(`monsterdamChicksApp.ChatRoomType.${chatRoomType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.title')}
                id="chat-room-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  maxLength: { value: 120, message: translate('entity.validation.maxlength', { max: 120 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.lastAction')}
                id="chat-room-lastAction"
                name="lastAction"
                data-cy="lastAction"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.lastConnectionDate')}
                id="chat-room-lastConnectionDate"
                name="lastConnectionDate"
                data-cy="lastConnectionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.createdDate')}
                id="chat-room-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.lastModifiedDate')}
                id="chat-room-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.createdBy')}
                id="chat-room-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.lastModifiedBy')}
                id="chat-room-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.deletedDate')}
                id="chat-room-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.chatRoom.participants')}
                id="chat-room-participants"
                data-cy="participants"
                type="select"
                multiple
                name="participants"
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chat-room" replace color="info">
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

export default ChatRoomUpdate;
