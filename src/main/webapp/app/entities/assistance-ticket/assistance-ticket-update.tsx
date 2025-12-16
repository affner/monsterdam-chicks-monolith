import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPlatformAdminUsers } from 'app/entities/platform-admin-user/platform-admin-user.reducer';
import { getEntities as getIdentityDocumentReviews } from 'app/entities/identity-document-review/identity-document-review.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { TicketType } from 'app/shared/model/enumerations/ticket-type.model';
import { createEntity, getEntity, reset, updateEntity } from './assistance-ticket.reducer';

export const AssistanceTicketUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const platformAdminUsers = useAppSelector(state => state.platformAdminUser.entities);
  const identityDocumentReviews = useAppSelector(state => state.identityDocumentReview.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const assistanceTicketEntity = useAppSelector(state => state.assistanceTicket.entity);
  const loading = useAppSelector(state => state.assistanceTicket.loading);
  const updating = useAppSelector(state => state.assistanceTicket.updating);
  const updateSuccess = useAppSelector(state => state.assistanceTicket.updateSuccess);
  const ticketStatusValues = Object.keys(TicketStatus);
  const ticketTypeValues = Object.keys(TicketType);

  const handleClose = () => {
    navigate(`/assistance-ticket${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlatformAdminUsers({}));
    dispatch(getIdentityDocumentReviews({}));
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
    values.openedAt = convertDateTimeToServer(values.openedAt);
    values.closedAt = convertDateTimeToServer(values.closedAt);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...assistanceTicketEntity,
      ...values,
      assignedAdmin: platformAdminUsers.find(it => it.id.toString() === values.assignedAdmin?.toString()),
      documentsReview: identityDocumentReviews.find(it => it.id.toString() === values.documentsReview?.toString()),
      user: userLites.find(it => it.id.toString() === values.user?.toString()),
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
          openedAt: displayDefaultDateTime(),
          closedAt: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'OPEN',
          type: 'ACCESS_ISSUE',
          ...assistanceTicketEntity,
          openedAt: convertDateTimeFromServer(assistanceTicketEntity.openedAt),
          closedAt: convertDateTimeFromServer(assistanceTicketEntity.closedAt),
          createdDate: convertDateTimeFromServer(assistanceTicketEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(assistanceTicketEntity.lastModifiedDate),
          assignedAdmin: assistanceTicketEntity?.assignedAdmin?.id,
          documentsReview: assistanceTicketEntity?.documentsReview?.id,
          user: assistanceTicketEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.assistanceTicket.home.createOrEditLabel" data-cy="AssistanceTicketCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.assistanceTicket.home.createOrEditLabel">
              Create or edit a AssistanceTicket
            </Translate>
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
                  id="assistance-ticket-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.subject')}
                id="assistance-ticket-subject"
                name="subject"
                data-cy="subject"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.description')}
                id="assistance-ticket-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.status')}
                id="assistance-ticket-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {ticketStatusValues.map(ticketStatus => (
                  <option value={ticketStatus} key={ticketStatus}>
                    {translate(`monsterdamChicksApp.TicketStatus.${ticketStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.type')}
                id="assistance-ticket-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {ticketTypeValues.map(ticketType => (
                  <option value={ticketType} key={ticketType}>
                    {translate(`monsterdamChicksApp.TicketType.${ticketType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.openedAt')}
                id="assistance-ticket-openedAt"
                name="openedAt"
                data-cy="openedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.closedAt')}
                id="assistance-ticket-closedAt"
                name="closedAt"
                data-cy="closedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.comments')}
                id="assistance-ticket-comments"
                name="comments"
                data-cy="comments"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.createdDate')}
                id="assistance-ticket-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.lastModifiedDate')}
                id="assistance-ticket-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.createdBy')}
                id="assistance-ticket-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.assistanceTicket.lastModifiedBy')}
                id="assistance-ticket-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                id="assistance-ticket-assignedAdmin"
                name="assignedAdmin"
                data-cy="assignedAdmin"
                label={translate('monsterdamChicksApp.assistanceTicket.assignedAdmin')}
                type="select"
              >
                <option value="" key="0" />
                {platformAdminUsers
                  ? platformAdminUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="assistance-ticket-documentsReview"
                name="documentsReview"
                data-cy="documentsReview"
                label={translate('monsterdamChicksApp.assistanceTicket.documentsReview')}
                type="select"
              >
                <option value="" key="0" />
                {identityDocumentReviews
                  ? identityDocumentReviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="assistance-ticket-user"
                name="user"
                data-cy="user"
                label={translate('monsterdamChicksApp.assistanceTicket.user')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/assistance-ticket" replace color="info">
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

export default AssistanceTicketUpdate;
